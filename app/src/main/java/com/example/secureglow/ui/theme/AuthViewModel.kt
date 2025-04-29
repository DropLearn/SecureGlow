package com.example.secureglow.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.secureglow.network.ApiClient
import com.example.secureglow.network.LoginRequest
import com.example.secureglow.network.OtpVerificationRequest
import com.example.secureglow.network.RegisterRequest
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class AuthViewModel : ViewModel() {
    private val apiService = ApiClient.authApiService

    fun register(
        firstName: String,
        lastName: String,
        emailAddress: String,
        phoneNumber: String,
        password: String,
        roles: List<String>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = apiService.register(
                    RegisterRequest(
                        firstName    = firstName,
                        lastName     = lastName,
                        emailAddress = emailAddress,
                        phoneNumber  = phoneNumber,
                        password     = password,
                        roles        = roles
                    )
                )
                val bodyString = response.body().orEmpty()
                when {
                    response.isSuccessful      -> onSuccess()
                    response.code() == 400     -> onError(bodyString)
                    else                       -> onError("Error ${response.code()}: $bodyString")
                }
            } catch (e: SocketTimeoutException) {
                onError("Connection timeout. Please try again.")
            } catch (e: Exception) {
                onError("Network error: ${e.message?.take(100)}")
            }
        }
    }

    fun verifyOtp(
        userName: String,
        otp: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = apiService.verifyOtp(
                    OtpVerificationRequest(
                        userName = userName,
                        otp      = otp
                    )
                )
                val bodyString = response.body().orEmpty()
                when {
                    response.isSuccessful      -> onSuccess()
                    response.code() == 400     -> onError(bodyString)
                    else                       -> onError("Error ${response.code()}: $bodyString")
                }
            } catch (e: Exception) {
                onError("Network error: ${e.message}")
            }
        }
    }

    fun login(
        userName: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = apiService.login(
                    LoginRequest(
                        userName = userName,
                        password = password
                    )
                )
                when {
                    response.isSuccessful      -> onSuccess()
                    response.code() == 400     -> onError("Invalid username or password")
                    else                       -> onError("Login failed with status ${response.code()}")
                }
            } catch (e: Exception) {
                onError("Network error: ${e.message}")
            }
        }
    }
}