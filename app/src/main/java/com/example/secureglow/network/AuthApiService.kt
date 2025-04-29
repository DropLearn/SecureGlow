package com.example.secureglow.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/api/v1/Auth/Register")
    suspend fun register(@Body request: RegisterRequest): Response<String>

    @POST("/api/v1/Auth/Login")
    suspend fun login(@Body request: LoginRequest): Response<String>

    @POST("/api/v1/Auth/ConfirmEmail")
    suspend fun verifyOtp(@Body request: OtpVerificationRequest): Response<String>
}

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val emailAddress: String,
    val phoneNumber: String,
    val password: String,
    val roles: List<String>
)

data class LoginRequest(
    val userName: String,
    val password: String
)

data class OtpVerificationRequest(
    val userName: String,
    val otp: String
)