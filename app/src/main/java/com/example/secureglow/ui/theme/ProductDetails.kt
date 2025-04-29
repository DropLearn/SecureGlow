package com.example.secureglow.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ProductDetailsScreen(navController: NavHostController, productId: String) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopHeader(title = "Product Details", navController)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Replace with actual product image loading
                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .background(Color.LightGray)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text("Product Name", color = Color.Black, style = MaterialTheme.typography.titleLarge)
                Text("Brand: SecureGlow Partner", color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Description:", color = Color.Black, style = MaterialTheme.typography.titleMedium)
                Text("Authentic product verified through SecureGlow's NFC verification system.", color = Color.DarkGray)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Verification Status: ✅ Authentic", color = Color(0xFF1B5E20))
            }
        }
    }
}