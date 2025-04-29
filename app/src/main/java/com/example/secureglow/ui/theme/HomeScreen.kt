package com.example.secureglow.ui.theme

import android.nfc.NfcAdapter
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: NfcViewModel) {
    val oceanBlue = Color(0xFF0277BD)
    val context = LocalContext.current
    val isScanning by viewModel.isScanning
    val tagContent by viewModel.tagContent
    val errorMessage by viewModel.errorMessage
    val nfcAdapter = remember { NfcAdapter.getDefaultAdapter(context) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("SecureGlow Scanner") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = oceanBlue,
                    titleContentColor = Color.White
                ),
                actions = {
                    TextButton(onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("login") { popUpTo("home") }
                    }) {
                        Text("Logout", color = Color.White)
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = oceanBlue
            ) {
                NavigationBarItem(
                    selected = currentRoute == "home",
                    onClick = { navController.navigate("home") },
                    icon = { Icon(Icons.Default.Home, "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = currentRoute == "history",
                    onClick = { navController.navigate("history") },
                    icon = { Icon(Icons.Default.History, "History") },
                    label = { Text("History") }
                )
                NavigationBarItem(
                    selected = currentRoute == "help",
                    onClick = { navController.navigate("help") },
                    icon = { Icon(Icons.Default.Help, "Help") },
                    label = { Text("Help") }
                )
                NavigationBarItem(
                    selected = currentRoute == "about",
                    onClick = { navController.navigate("about") },
                    icon = { Icon(Icons.Default.Info, "About") },
                    label = { Text("About") }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(oceanBlue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        if (!isScanning) {
                            if (nfcAdapter != null) {
                                viewModel.startScanSession()
                                Toast.makeText(context, "Hold device near NFC tag", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "NFC not available on this device", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isScanning) Color.Gray else oceanBlue,
                        contentColor = Color.White
                    ),
                    enabled = !isScanning && nfcAdapter != null
                ) {
                    Text(text = if (isScanning) "Scanning..." else "Tap to Scan")
                }

                Spacer(modifier = Modifier.height(32.dp))

                when {
                    isScanning -> CircularProgressIndicator(color = Color.White)
                    !errorMessage.isNullOrEmpty() -> NFCResultDisplay(errorMessage!!, true)
                    tagContent.isNotEmpty() -> NFCResultDisplay(tagContent, false)
                }
            }
        }
    }
}

@Composable
fun NFCResultDisplay(message: String, isError: Boolean) {
    val backgroundColor = if (isError) Color(0xFFFFCDD2) else Color(0xFFC8E6C9)
    val textColor = if (isError) Color(0xFFB71C1C) else Color(0xFF1B5E20)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = if (isError) Icons.Default.Error else Icons.Default.CheckCircle,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (isError) "Error" else "Tag Scanned",
                color = textColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message,
                color = textColor,
                textAlign = TextAlign.Center
            )
        }
    }
}