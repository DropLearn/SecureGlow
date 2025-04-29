package com.example.secureglow.ui.theme


import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Displays an "S" logo, SecureGlow title, and a fading tagline.
 * Calls [onSplashFinished] after 4 seconds.
 */
@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val fadeAnim = rememberInfiniteTransition()
    val alpha by fadeAnim.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(Unit) {
        delay(4000L)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0277BD)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "S", fontSize = 72.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "SecureGlow", fontSize = 24.sp, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Revolutionizing trust\nin a connected world",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.alpha(alpha),
                textAlign = TextAlign.Center
            )
        }
    }
}
