package com.sagrd.spellingappv2.presentation.logros

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.sagrd.spellingappv2.data.remote.Resource
import com.sagrd.spellingappv2.data.remote.dto.LogrosDto
import com.sagrd.spellingappv2.data.repository.LogroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogrosViewModel @Inject constructor(
    private val logrosRepository: LogroRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LogrosUiState())
    val uiState = _uiState.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    init {
        getLogros()
    }

    fun save() {
        viewModelScope.launch {
            if (isValid()) {
                logrosRepository.save(_uiState.value.toEntity())
            } else {
                _uiState.update {
                    it.copy(
                        errorMessage = "Todos los campos son obligatorios"
                    )
                }
            }
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch {
            logrosRepository.delete(id)
        }
    }

    fun update() {
        viewModelScope.launch {
            logrosRepository.update(
                _uiState.value.logroId, LogrosDto(
                    logroId = _uiState.value.logroId,
                    nombreCompleto = _uiState.value.nombreCompleto,
                    mensaje = _uiState.value.mensaje
                )
            )
        }
    }

    fun new() {
        _uiState.value = LogrosUiState()
    }

    fun onNombreCompletoChange(nombreCompleto: String) {
        _uiState.update {
            it.copy(
                nombreCompleto = nombreCompleto,
                errorMessage = if (nombreCompleto.isBlank()) "Debes rellenar el campo Nombre Completo"
                else null
            )
        }
    }

    fun onMensajeChange(mensaje: String) {
        _uiState.update {
            it.copy(
                mensaje = mensaje,
                errorMessage = if (mensaje.isBlank()) "Debes rellenar el campo Mensaje"
                else null
            )
        }
    }

    fun find(logroId: Int) {
        viewModelScope.launch {
            if (logroId > 0) {
                val logrosDto = logrosRepository.find(logroId)
                if (logrosDto.logroId != 0) {
                    _uiState.update {
                        it.copy(
                            logroId = logrosDto.logroId,
                            nombreCompleto = logrosDto.nombreCompleto,
                            mensaje = logrosDto.mensaje
                        )
                    }
                }
            }
        }
    }

    fun getLogros() {
        viewModelScope.launch {
            logrosRepository.getLogros().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                logros = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = result.message ?: "Error desconocido",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun isValid(): Boolean {
        return uiState.value.nombreCompleto.isNotBlank() &&
                uiState.value.mensaje.isNotBlank()
    }
}

fun LogrosUiState.toEntity() = LogrosDto(
    logroId = logroId,
    nombreCompleto = nombreCompleto,
    mensaje = mensaje
)

data class LogrosUiState(
    val logroId: Int = 0,
    val nombreCompleto: String = "",
    val mensaje: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val logros: List<LogrosDto> = emptyList()
)