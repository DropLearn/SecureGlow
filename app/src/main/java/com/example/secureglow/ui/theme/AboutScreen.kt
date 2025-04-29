package com.example.secureglow.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun AboutScreen(navController: NavHostController) {
    val oceanBlue = Color(0xFF0277BD)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopHeader(title = "About", navController)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("SecureGlow",
                    style = MaterialTheme.typography.titleLarge,
                    color = oceanBlue,
                    textAlign = TextAlign.Center)

                Text("Version 1.0.0",
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp))

                Spacer(modifier = Modifier.height(24.dp))

                SectionTitle("Developed By")
                Text("TESERA Solutions\nInnovation Street\nKILIMANI",
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center)

                Spacer(modifier = Modifier.height(24.dp))

                SectionTitle("Privacy Policy")
                Text(
                    text = "SecureGlow is committed to protecting your privacy. This policy explains:\n\n" +
                            "• Data Collection: We collect NFC tag UIDs, scan timestamps, and basic device information " +
                            "for verification purposes.\n\n" +
                            "• Data Usage: Information is used solely for product authentication and improving our services. " +
                            "We never sell user data.\n\n" +
                            "• Data Security: All data is encrypted using AES-256 and transmitted over secure TLS connections.\n\n" +
                            "• Retention: Scan history is stored for 30 days unless manually deleted by the user.\n\n" +
                            "• User Rights: You may request data access, correction, or deletion via app settings or " +
                            "by contacting support.\n\n" +
                            "• Third Parties: We only share data with verified manufacturers during authentication checks.\n\n" +
                            "• Updates: Policy changes will be notified through app updates.",
                    color = Color.DarkGray,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                SectionTitle("Legal")
                Text("© 2025 TESERA Solutions\nAll rights reserved",
                    color = Color.Gray,
                    textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = Color(0xFF0277BD),
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )

    Divider(
        color = Color(0xFF0277BD).copy(alpha = 0.2f),
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}