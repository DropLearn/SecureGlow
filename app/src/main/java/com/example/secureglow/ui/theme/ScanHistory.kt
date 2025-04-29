package com.example.secureglow.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ScanHistoryScreen(
    navController: NavHostController,
    viewModel: NfcViewModel
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopHeader(title = "Scan History", navController)

        if (viewModel.scanHistory.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No scan history available", color = Color(0xFF0277BD))
            }
        } else {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = { viewModel.clearHistory() }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Clear History",
                                tint = Color(0xFF0277BD)
                            )
                        }
                    }
                }

                items(viewModel.scanHistory) { item ->
                    ScanHistoryItem(item) {
                        navController.navigate("productDetails/${item.id}")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun ScanHistoryItem(
    item: ScanHistoryItem,
    onItemClick: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                width = 1.dp,
                color = Color(0xFF0277BD),
                shape = RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clickable { onItemClick() }
        ) {
            Text("Tag: ${item.content}", color = Color.Black)
            Text("Date: ${dateFormat.format(item.timestamp)}", color = Color.Gray)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = if (item.authentic) Icons.Default.CheckCircle else Icons.Default.Error,
                    contentDescription = null,
                    tint = if (item.authentic) Color(0xFF1B5E20) else Color(0xFFB71C1C)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (item.authentic) "Authentic" else "Fake",
                    color = if (item.authentic) Color(0xFF1B5E20) else Color(0xFFB71C1C)
                )
            }
        }
    }
}