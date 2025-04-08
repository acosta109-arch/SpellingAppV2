package com.sagrd.spellingappv2.presentation.hijos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagrd.spellingappv2.data.remote.Resource
import com.sagrd.spellingappv2.data.repository.HijoRepository
import com.sagrd.spellingappv2.data.repository.PinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HijosViewModel @Inject constructor(
    private val repository: HijoRepository,
    private val pinRepository: PinRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HijoUistate())
    val uiState = _uiState.asStateFlow()

    init {
        getHijos()
        getPines()
    }

    fun onEvent(event: HijosEvent) {
        when(event) {
            is HijosEvent.OnNombreChange -> {
                _uiState.update { it.copy(nombre = event.dato, errorNombre = null) }
            }
            is HijosEvent.OnApellidoChange -> {
                _uiState.update { it.copy(apellido = event.dato, errorApellido = null) }
            }
            is HijosEvent.OnGeneroChange -> {
                _uiState.update { it.copy(genero = event.dato, errorGenero = null) }
            }
            is HijosEvent.OnEdadChange -> {
                _uiState.update {
                    it.copy(
                        edad = event.dato.toIntOrNull() ?: 0,
                        errorEdad = null
                    )
                }
            }
            is HijosEvent.OnPinChange -> {
                _uiState.update {
                    it.copy(
                        pinId = event.dato,
                        errorPinId = null
                    )
                }
            }
            is HijosEvent.OnUsuarioIdChange -> {
                _uiState.update { it.copy(usuarioId = event.dato.toIntOrNull() ?: 0) }
            }
            is HijosEvent.OnSave -> {
                saveHijo()
            }
            is HijosEvent.OnDelete -> {
                deleteHijo()
            }
        }
    }

    fun saveHijo(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            if (!validateInputs()) {
                return@launch
            }

            repository.saveHijo(_uiState.value.toEntity())
            _uiState.update {
                it.copy(
                    errorMessage = null,
                    errorNombre = null,
                    errorApellido = null,
                    errorGenero = null,
                    errorEdad = null,
                    errorPinId = null,
                    successMessage = "Hijo guardado exitosamente"
                )
            }
            nuevo()
            onSuccess()
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        if (_uiState.value.nombre.isBlank()) {
            _uiState.update { it.copy(errorNombre = "El nombre no puede estar vacío.") }
            isValid = false
        }

        if (_uiState.value.apellido.isBlank()) {
            _uiState.update { it.copy(errorApellido = "El apellido no puede estar vacío.")}
            isValid = false
        }

        if (_uiState.value.genero.isBlank()) {
            _uiState.update { it.copy(errorGenero = "El género no puede estar vacío.")}
            isValid = false
        }

        if (_uiState.value.edad == 0) {
            _uiState.update { it.copy(errorEdad = "La edad no puede ser cero.")}
            isValid = false
        }

        if (_uiState.value.pinId.isBlank()) {
            _uiState.update { it.copy(errorPinId = "Debe asignarle un pin a su hijo.")}
            isValid = false
        } else if (_uiState.value.usedPins.contains(_uiState.value.pinId) && _uiState.value.hijoId == null) {
            _uiState.update { it.copy(errorPinId = "Este pin ya está siendo utilizado.")}
            isValid = false
        }

        if (isDuplicateName()) {
            _uiState.update { it.copy(errorNombre = "Ya existe un hijo con este nombre.")}
            isValid = false
        }

        if (!isValid) {
            _uiState.update { it.copy(successMessage = null)}
        }

        return isValid
    }

    fun nuevo() {
        _uiState.value = HijoUistate()
    }

    private fun isDuplicateName(): Boolean {
        val nombre = _uiState.value.nombre.trim().lowercase()

        return _uiState.value.hijos.any { hijo ->
            val hijoNombre = hijo.nombre.trim().lowercase()
            hijoNombre == nombre && hijo.hijoId != _uiState.value.hijoId
        }
    }

    private fun getHijos() {
        viewModelScope.launch {
            repository.getAllHijos().collect { hijos ->
                _uiState.update {
                    it.copy(
                        hijos = hijos,
                    )
                }
            }
        }
    }

    private fun getPines() {
        viewModelScope.launch {
            pinRepository.getPines().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val usedPins = repository.getAllHijos().first()
                            .map { it.pinId }
                            .toSet()

                        _uiState.update {
                            it.copy(
                                pines = resource.data ?: emptyList(),
                                errorMessage = null,
                                usedPins = usedPins
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                pines = emptyList(),
                                errorMessage = resource.message
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(errorMessage = null)
                        }
                    }
                }
            }
        }
    }

    fun selectedHijos(hijoId: Int) {
        viewModelScope.launch {
            if (hijoId > 0) {
                val hijo = repository.getHijoById(hijoId)
                _uiState.update {
                    it.copy(
                        hijoId = hijo?.hijoId,
                        nombre = hijo?.nombre ?: "",
                        apellido = hijo?.apellido ?: "",
                        genero = hijo?.genero ?: "",
                        edad = hijo?.edad ?: 0,
                        pinId = hijo?.pinId ?: "",
                        usuarioId = hijo?.usuarioId ?: 0
                    )
                }
            }
        }
    }

    fun deleteHijo() {
        viewModelScope.launch {
            repository.deleteHijo(_uiState.value.toEntity())
        }
    }
}
