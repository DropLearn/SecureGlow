package com.example.secureglow.ui.theme

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class ScanHistoryItem(
    val id: String,
    val content: String,
    val timestamp: Long,
    val authentic: Boolean
)

class NfcViewModel : ViewModel() {
    private val _isScanning = mutableStateOf(false)
    val isScanning: State<Boolean> = _isScanning

    private val _tagContent = mutableStateOf("")
    val tagContent: State<String> = _tagContent

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _scanHistory = mutableStateOf<List<ScanHistoryItem>>(emptyList())
    val scanHistory: List<ScanHistoryItem> get() = _scanHistory.value

    fun startScanSession() {
        _isScanning.value = true
        _tagContent.value = ""
        _errorMessage.value = null
    }

    fun stopScanSession() {
        _isScanning.value = false
    }


    fun handleTag(content: String) {
        viewModelScope.launch {
            _tagContent.value = content
            _isScanning.value = false
            _scanHistory.value += ScanHistoryItem(
                id = generateUniqueId(),
                content = content,
                timestamp = System.currentTimeMillis(),
                authentic = checkAuthenticity(content)
            )
        }
    }

    fun handleError(error: String) {
        viewModelScope.launch {
            _errorMessage.value = error
            _isScanning.value = false
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            _scanHistory.value = emptyList()
        }
    }

    private fun generateUniqueId(): String {
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault())
        return "scan_${dateFormat.format(Date())}"
    }

    private fun checkAuthenticity(content: String): Boolean {
        return content.contains("secureglow", ignoreCase = true)
    }
}