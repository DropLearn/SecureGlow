
package com.example.secureglow.ui.theme

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import java.util.regex.Pattern

@Composable
fun SignUpScreen(navController: NavHostController) {
    val oceanBlue = Color(0xFF0277BD)
    var firstName by remember { mutableStateOf("") }
    var lastName  by remember { mutableStateOf("") }
    var email     by remember { mutableStateOf("") }
    var phone     by remember { mutableStateOf("") }
    var password  by remember { mutableStateOf("") }
    var rolesInput by remember { mutableStateOf("") }
    var isLoading   by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val viewModel: AuthViewModel = viewModel()

    fun isValidPhoneNumber(phone: String): Boolean {
        val pattern = Pattern.compile("^[0-9]{10,15}$")
        return pattern.matcher(phone).matches()
    }

    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = { errorMessage = null },
            title = { Text("Registration Error") },
            text  = { Text(errorMessage ?: "Unknown error") },
            confirmButton = {
                Button(
                    onClick = { errorMessage = null },
                    colors = ButtonDefaults.buttonColors(containerColor = oceanBlue)
                ) {
                    Text("OK")
                }
            }
        )
    }

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
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("S", fontSize = 48.sp, color = oceanBlue)
            Text(
                "Sign Up",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(oceanBlue),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = {
                    if (it.all { c -> c.isDigit() }) phone = it
                },
                label = { Text("Phone Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = rolesInput,
                onValueChange = { rolesInput = it },
                label = { Text("Roles") },
                placeholder = { Text("subscriber") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    when {
                        firstName.isBlank() ||
                                lastName.isBlank()  ||
                                email.isBlank()     ||
                                phone.isBlank()     ||
                                password.isBlank()  ||
                                rolesInput.isBlank() -> {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                        }
                        !isValidPhoneNumber(phone) -> {
                            Toast.makeText(context, "Enter a valid 10-15 digit phone number", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            val rolesList = rolesInput
                                .split(',')
                                .map { it.trim() }
                                .filter { it.isNotEmpty() }

                            isLoading = true
                            viewModel.register(
                                firstName,
                                lastName,
                                email,
                                phone,
                                password,
                                rolesList,
                                onSuccess = {
                                    isLoading = false
                                    navController.navigate("otp/$email") {
                                        popUpTo("signup") { inclusive = true }
                                    }
                                },
                                onError = { error ->
                                    isLoading = false
                                    errorMessage = error
                                }
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = oceanBlue,
                    contentColor = Color.White
                )
            ) {
                Text("Sign Up")
            }

            if (isLoading) {
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate("login") }) {
                Text("Already have an account? Login", color = oceanBlue)
            }
        }
    }
}
