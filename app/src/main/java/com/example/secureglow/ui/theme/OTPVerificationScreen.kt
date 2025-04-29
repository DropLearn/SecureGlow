package com.example.secureglow.ui.theme

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun OtpVerificationScreen(navController: NavHostController, userName: String) {
    val oceanBlue = Color(0xFF0277BD)
    var otp       by remember { mutableStateOf(TextFieldValue("")) }
    var isLoading by remember { mutableStateOf(false) }
    val context   = LocalContext.current
    val viewModel: AuthViewModel = viewModel()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(oceanBlue),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color.White, shape = RoundedCornerShape(20.dp))
                .border(2.dp, oceanBlue, shape = RoundedCornerShape(20.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("S", fontSize = 48.sp, color = oceanBlue)
            Text(
                "Verify Email",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(oceanBlue),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            Text(
                "Code sent to\n$userName",
                color = oceanBlue,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = otp,
                onValueChange = { if (it.text.length <= 6) otp = it },
                label = { Text("OTP Code") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (otp.text.length != 6) {
                        Toast.makeText(context, "Enter 6-digit code", Toast.LENGTH_SHORT).show()
                    } else {
                        isLoading = true
                        viewModel.verifyOtp(
                            userName = userName,
                            otp      = otp.text,
                            onSuccess = {
                                isLoading = false
                                navController.navigate("login") {
                                    popUpTo("otp/$userName") { inclusive = true }
                                }
                            },
                            onError = { error ->
                                isLoading = false
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = oceanBlue,
                    contentColor = Color.White
                )
            ) {
                Text("Verify")
            }

            if (isLoading) Spacer(Modifier.height(16.dp)).also { CircularProgressIndicator() }
        }
    }
}
