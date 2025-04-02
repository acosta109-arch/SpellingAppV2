package com.sagrd.spellingappv2.presentation.loginHijo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagrd.spellingappv2.data.local.entities.PinEntity
import com.sagrd.spellingappv2.data.remote.Resource
import com.sagrd.spellingappv2.data.repository.PinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginHijoViewModel @Inject constructor(
    private val repository: PinRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPines()
    }

    private fun loadPines() {
        viewModelScope.launch {
            repository.getPines().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                registeredPins = resource.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = resource.message,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun onPinChange(pin: String) {
        _uiState.update {
            it.copy(
                pin = pin,
                errorMessage = null
            )
        }
    }

    fun login() {
        viewModelScope.launch {
            val validationError = validate()
            if (validationError != null) {
                _uiState.update {
                    it.copy(
                        errorMessage = validationError,
                        isLoginSuccessful = false
                    )
                }
                return@launch
            }

            val existingPin = uiState.value.registeredPins
                .firstOrNull { it.pin == uiState.value.pin }

            if (existingPin != null) {
                _uiState.update {
                    it.copy(
                        isLoginSuccessful = true,
                        errorMessage = null,
                        successMessage = "Inicio de sesión exitoso."
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoginSuccessful = false,
                        errorMessage = "Pin incorrecto.",
                        successMessage = null
                    )
                }
            }
        }
    }

    private fun validate(): String? {
        return when {
            uiState.value.pin.isBlank() -> "El pin no puede estar vacío."
            uiState.value.pin.length < 4 -> "El pin debe tener al menos 4 dígitos."
            else -> null
        }
    }
}

data class LoginUiState(
    val pin: String = "",
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isLoginSuccessful: Boolean = false,
    val registeredPins: List<PinEntity> = emptyList(),
    val isLoading: Boolean = false
)