package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.ManufacturerInfo
import com.example.repository.VinIntervalRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    VIN_CHECK
}

sealed interface VinCheckState {
    object Idle : VinCheckState
    object Loading : VinCheckState
    data class Success(val info: ManufacturerInfo, val vin: String) : VinCheckState
    data class Error(val message: String) : VinCheckState
}

class VinViewModel : ViewModel() {

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    //exposing read-only state
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _vinInput = MutableStateFlow("")
    //exposing read-only state
    val vinInput: StateFlow<String> = _vinInput.asStateFlow()

    private val _checkState = MutableStateFlow<VinCheckState>(VinCheckState.Idle)
    //exposing read-only state
    val checkState: StateFlow<VinCheckState> = _checkState.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
        if (screen == AppScreen.HOME) {
            // Reset state when heading back to Home
            _vinInput.value = ""
            _checkState.value = VinCheckState.Idle
        }
    }

    //this is called when the user inputs a new VIN without deleting the old input from the input field
    fun updateVin(vin: String) {
        // VINs are alphanumeric, up to 17 characters
        val cleanVin = vin.filter { it.isLetterOrDigit() }.uppercase()
        if (cleanVin.length <= 17) {
            _vinInput.value = cleanVin
            
            // If we are currently showing a Success profile, let's transition it back to idle
            // so they click Decode to see the stylistic loading circle again as requested
            if (_checkState.value is VinCheckState.Success || _checkState.value is VinCheckState.Error) {
                _checkState.value = VinCheckState.Idle
            }
        }
    }

    fun prefillVin(prefix: String) {
        // Autofill a test mock VIN for swift inspection
        val randomSuffix = "9A2BC3D4E5FGHIJ"
        val mockFullVin = (prefix + randomSuffix).take(17).uppercase()
        updateVin(mockFullVin)
        checkServiceIntervals()
    }

    fun checkServiceIntervals() {
        val vin = _vinInput.value.trim()
        
        if (vin.length < 3) {
            _checkState.value = VinCheckState.Error("Please enter at least 3 characters to decode a manufacturer prefix (e.g., 'WBA', 'WDB', '5YJ').")
            return
        }

        viewModelScope.launch {
            _checkState.value = VinCheckState.Loading
            
            // Stylistic verification delay (250ms) as requested by the user
            delay(250)
            
            try {
                val decodedInfo = VinIntervalRepository.decodeVin(vin)
                _checkState.value = VinCheckState.Success(decodedInfo, vin)
            } catch (e: Exception) {
                _checkState.value = VinCheckState.Error("An error occurred during decoding: ${e.localizedMessage}")
            }
        }
    }
}
