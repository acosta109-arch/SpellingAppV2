package com.sagrd.spellingappv2.presentation.pin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagrd.spellingappv2.data.local.entities.PinEntity
import com.sagrd.spellingappv2.data.remote.Resource
import com.sagrd.spellingappv2.data.remote.dto.PinesDto
import com.sagrd.spellingappv2.data.repository.PinRepository
import com.sagrd.spellingappv2.data.repository.HijoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PinViewModel @Inject constructor(
    private val repository: PinRepository,
    private val hijoRepository: HijoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PinUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPines()
    }

    fun onEvent(event: PinEvent) {
        when (event) {
            is PinEvent.OnPinChange -> {
                _uiState.update {
                    it.copy(
                        pin = event.dato,
                        errorPin = null,
                        errorMessage = null
                    )
                }
            }

            is PinEvent.OnSave -> {
                savePin()
            }

            is PinEvent.OnHideDialog -> {
                hideDeleteDialog()
            }

            is PinEvent.CheckPinUsage -> {
                checkPinUsage()
            }

            is PinEvent.OnDelete -> {
                deletePin()
            }
        }
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
            if (!validatePin()) {
                return@launch
            }

            try {
                val pineDto = PinesDto(
                    pinId = _uiState.value.pinId ?: 0,
                    pin = _uiState.value.pin
                )

                if (_uiState.value.pinId == null) {
                    repository.save(pineDto)
                } else {
                    repository.update(_uiState.value.pinId!!, pineDto)
                }

                _uiState.update {
                    it.copy(
                        successMessage = "Pin guardado correctamente.",
                        errorMessage = null,
                        errorPin = null
                    )
                }
                loadPines()
                _uiState.update {
                    it.copy(pin = "", pinId = null)
                }
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

    private fun validatePin(): Boolean {
        when {
            _uiState.value.pin.isBlank() -> {
                _uiState.update {
                    it.copy(
                        errorPin = "El pin no puede estar vacío.",
                        successMessage = null
                    )
                }
                return false
            }

            _uiState.value.pins.any {
                it.pin == _uiState.value.pin && it.pinId != _uiState.value.pinId
            } -> {
                _uiState.update {
                    it.copy(
                        errorPin = "Este pin ya existe. Por favor, elija otro.",
                        successMessage = null
                    )
                }
                return false
            }

            else -> return true
        }
    }

    fun checkPinUsage() {
        viewModelScope.launch {
            val pinId = uiState.value.pinId
            if (pinId != null) {
                try {
                    val hijos = hijoRepository.getAllHijos().first()
                    val hijoUsingPin = hijos.find { it.pinId == pinId.toString() }

                    if (hijoUsingPin != null) {
                        _uiState.update {
                            it.copy(
                                showDeleteDialog = true,
                                canDelete = false,
                                hijoUsingPin = "${hijoUsingPin.nombre} ${hijoUsingPin.apellido}"
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                showDeleteDialog = true,
                                canDelete = true,
                                hijoUsingPin = null
                            )
                        }
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(
                            errorMessage = "Error al verificar el uso del pin: ${e.message}",
                            successMessage = null
                        )
                    }
                }
            }
        }
    }

    fun hideDeleteDialog() {
        _uiState.update {
            it.copy(
                showDeleteDialog = false,
                canDelete = false,
                hijoUsingPin = null
            )
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
                            errorMessage = null,
                            showDeleteDialog = false
                        )
                    }
                    loadPines()
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(
                            errorMessage = "Error al eliminar el pin: ${e.message}",
                            successMessage = null,
                            showDeleteDialog = false
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
}