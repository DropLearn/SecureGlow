package com.example.secureglow

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.secureglow.ui.theme.*
import java.nio.charset.Charset

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: NfcViewModel
    private val nfcAdapter: NfcAdapter? by lazy { NfcAdapter.getDefaultAdapter(this) }
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel = ViewModelProvider(this)[NfcViewModel::class.java]
        pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE
        )

        setContent {
            SecureGlowTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "splash"
                ) {
                    composable("splash") {
                        SplashScreen { navController.navigate("login") }
                    }
                    composable("login") { LoginScreen(navController) }
                    composable("signup") { SignUpScreen(navController) }
                    composable("reset") { ResetPasswordScreen(navController) }
                    composable("home") { HomeScreen(navController, viewModel) }
                    composable("history") { ScanHistoryScreen(navController, viewModel) }
                    composable("productDetails/{productId}") { backStackEntry ->
                        ProductDetailsScreen(
                            navController,
                            backStackEntry.arguments?.getString("productId") ?: ""
                        )
                    }
                    composable("help") { HelpFAQScreen(navController) }
                    composable("about") { AboutScreen(navController) }
                    composable("otp/{email}") { backStackEntry ->
                        OtpVerificationScreen(
                            navController,
                            backStackEntry.arguments?.getString("email") ?: ""
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.let { adapter ->
            if (viewModel.isScanning.value) {
                adapter.enableForegroundDispatch(
                    this,
                    pendingIntent,
                    arrayOf(IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)),
                    arrayOf(arrayOf(Ndef::class.java.name))
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (viewModel.isScanning.value) {
            processTag(intent)
        }
    }

    private fun processTag(intent: Intent) {
        val tag: Tag? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        }

        tag?.let {
            try {
                Ndef.get(it)?.let { ndef ->
                    val message = readNdefMessage(ndef)
                    viewModel.handleTag(message)
                } ?: viewModel.handleError("Tag not NDEF formatted")
            } catch (e: Exception) {
                viewModel.handleError("Read error: ${e.message}")
            } finally {
                viewModel.stopScanSession()
            }
        } ?: viewModel.handleError("Invalid tag")
    }

    private fun readNdefMessage(ndef: Ndef): String {
        return ndef.use { ndef ->
            ndef.connect()
            ndef.ndefMessage?.records?.joinToString("\n") { record ->
                if (record.toMimeType() == "text/plain") {
                    String(record.payload, Charset.forName("UTF-8")).drop(1)
                } else {
                    "Unsupported format"
                }
            } ?: "Empty tag"
        }
    }
}