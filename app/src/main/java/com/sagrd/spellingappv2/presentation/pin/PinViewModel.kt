package com.sagrd.spellingappv2.presentation.pin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagrd.spellingappv2.data.local.entities.PinEntity
import com.sagrd.spellingappv2.data.repository.PinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PinViewModel @Inject constructor(
    private val repository: PinRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(Uistate())
    val uiState = _uiState.asStateFlow()

    init {
        getPins()
    }

    fun savePin() {
        viewModelScope.launch {
            val validationError = validate()
            if (validationError != null) {
                _uiState.update {
                    it.copy(errorMessage = validationError, successMessage = null)
                }
            } else{

                if (repository.validarPin(_uiState.value.pin)) {
                    _uiState.update {
                        it.copy(errorMessage = "El pin ya está creado.", successMessage = null)
                    }
                } else {
                    repository.savePin(_uiState.value.toEntity())
                    _uiState.update {
                        it.copy(successMessage = "Pin guardado correctamente.", errorMessage = null)
                    }
                }


            }
        }
    }

    private fun validate(): String? {
        return when {
            _uiState.value.pin.isBlank() -> "El pin no puede estar vacío."
            else -> null
        }
    }

    fun nuevo(){
        _uiState.value = Uistate()
    }

    fun deletePin() {
        viewModelScope.launch {
            repository.deletePin(_uiState.value.toEntity())
        }
    }

    private fun getPins() {
        viewModelScope.launch {
            repository.getAllPines().collect { pins ->
                _uiState.update {
                    it.copy(pins = pins)
                }
            }
        }
    }

    fun selectedPines(pinId: Int) {
        viewModelScope.launch{
            if (pinId > 0) {
                val pin = repository.getPinById(pinId)
                _uiState.update {
                    it.copy(
                        pinId = pin?.pinId,
                        pin = pin?.pin ?: ""
                    )
                }
            }
        }
    }

    fun onPinChange(pin: String) {
        _uiState.update {
            it.copy(pin = pin)
        }
    }



}

data class Uistate(
    val pinId: Int? = null,
    val pin: String = "",
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val pins: List<PinEntity> = emptyList()
)

fun Uistate.toEntity() = PinEntity(
    pinId = pinId,
    pin = pin,
)