package com.example.secureglow.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


// FAQ data class definition
data class FAQ(
    val question: String,
    val answer: String
)


@Composable
fun HelpFAQScreen(navController: NavHostController) {
    val oceanBlue = Color(0xFF0277BD)
    val faqItems = remember {
        listOf(
            FAQ("How do I scan a product?", "Open the app and tap the 'Scan' button..."),
            FAQ("What if scanning fails?", "1. Ensure NFC is enabled..."),
            FAQ("Is my data secure?", "All scan data is encrypted..."),
            FAQ("How to delete history?", "Go to Scan History screen...")
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopHeader(title = "Help & FAQ", navController)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(faqItems) { item ->
                FAQItem(item)
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Contact Support",
                        color = oceanBlue,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp))

                    Text("Support Line: +254 721 956 970\n\n" +
                            "Email: support@secureglow.com\n\n" +
                            "Business Hours: Mon-Fri 9AM-5PM (GMT)",
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center)

                    Spacer(modifier = Modifier.height(24.dp))

                    Text("App Version: 1.0.0\nLast Updated: March 2025",
                        color = Color.Gray,
                        textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
fun FAQItem(faq: FAQ) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(0.95f),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(faq.question,
                    color = Color(0xFF0277BD),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start)

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Color(0xFF0277BD))
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(faq.answer,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Start)
            }
        }
    }
}