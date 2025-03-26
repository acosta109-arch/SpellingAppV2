package com.sagrd.spellingappv2.presentation.pin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagrd.spellingappv2.data.local.entities.PinEntity
import com.sagrd.spellingappv2.data.remote.Resource
import com.sagrd.spellingappv2.data.remote.dto.PinesDto
import com.sagrd.spellingappv2.data.repository.PinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PinViewModel @Inject constructor(
    private val repository: PinRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPines()
    }

    fun loadPines() {
        viewModelScope.launch {
            repository.getPines().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                pins = resource.data ?: emptyList(),
                                isLoading = false
                            )
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

    fun savePin() {
        viewModelScope.launch {
            val validationError = validate()
            if (validationError != null) {
                _uiState.update {
                    it.copy(errorMessage = validationError, successMessage = null)
                }
                return@launch
            }

            try {
                val pineDto = PinesDto(
                    pinId = uiState.value.pinId ?: 0,
                    pin = uiState.value.pin
                )

                if (uiState.value.pinId == null) {
                    repository.save(pineDto)
                } else {
                    repository.update(uiState.value.pinId!!, pineDto)
                }

                _uiState.update {
                    it.copy(
                        successMessage = "Pin guardado correctamente.",
                        errorMessage = null
                    )
                }
                loadPines()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Error al guardar el pin: ${e.message}",
                        successMessage = null
                    )
                }
            }
        }
    }

    fun deletePin() {
        viewModelScope.launch {
            val pinId = uiState.value.pinId
            if (pinId != null) {
                try {
                    repository.delete(pinId)
                    _uiState.update {
                        it.copy(
                            successMessage = "Pin eliminado correctamente.",
                            errorMessage = null
                        )
                    }
                    loadPines()
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(
                            errorMessage = "Error al eliminar el pin: ${e.message}",
                            successMessage = null
                        )
                    }
                }
            }
        }
    }

    fun selectedPines(pinId: Int) {
        viewModelScope.launch {
            try {
                val pin = repository.find(pinId)
                pin?.let {
                    _uiState.update {
                        it.copy(
                            pinId = pin.pinId,
                            pin = pin.pin
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Error al seleccionar el pin: ${e.message}")
                }
            }
        }
    }

    fun onPinChange(pin: String) {
        _uiState.update {
            it.copy(pin = pin)
        }
    }

    private fun validate(): String? {
        return when {
            uiState.value.pin.isBlank() -> "El pin no puede estar vacío."
            uiState.value.pins.any {
                it.pin == uiState.value.pin &&
                        it.pinId != uiState.value.pinId
            } -> "Este pin ya existe. Por favor, elija otro."
            else -> null
        }
    }

    fun resetState() {
        _uiState.value = UiState()
    }
}


data class UiState(
    val pinId: Int? = null,
    val pin: String = "",
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val pins: List<PinEntity> = emptyList(),
    val isLoading: Boolean = false
)