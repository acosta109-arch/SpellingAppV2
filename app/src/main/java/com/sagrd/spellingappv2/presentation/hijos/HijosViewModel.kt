package com.sagrd.spellingappv2.presentation.hijos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagrd.spellingappv2.data.local.entities.HijoEntity
import com.sagrd.spellingappv2.data.local.entities.PinEntity
import com.sagrd.spellingappv2.data.remote.Resource
import com.sagrd.spellingappv2.data.repository.HijoRepository
import com.sagrd.spellingappv2.data.repository.PinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class hijosViewModel @Inject constructor(
    private val repository: HijoRepository,
    private val pinRepository: PinRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(Uistate())
    val uiState = _uiState.asStateFlow()

    // Track whether save should be prevented
    private var shouldPreventSave = false

    init {
        getHijos()
        getPines()
    }

    fun saveHijo(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val validationError = validate()
            if (validationError != null) {
                // Set error message and prevent navigation
                _uiState.update {
                    it.copy(
                        errorMessage = validationError,
                        successMessage = null
                    )
                }
                shouldPreventSave = true
            } else {
                val duplicateChild = repository.getAllHijos().first().find { hijo ->
                    hijo.nombre.equals(_uiState.value.nombre, ignoreCase = true) &&
                            hijo.pinId == _uiState.value.pinId &&
                            hijo.hijoId != _uiState.value.hijoId
                }

                if (duplicateChild != null) {
                    // Set duplicate error and prevent navigation
                    _uiState.update {
                        it.copy(
                            errorMessage = "Ya existe un hijo con este nombre y pin.",
                            successMessage = null
                        )
                    }
                    shouldPreventSave = true
                } else {
                    // Successful save
                    repository.saveHijo(_uiState.value.toEntity())
                    _uiState.update {
                        it.copy(
                            errorMessage = null,
                            successMessage = "Hijo guardado exitosamente"
                        )
                    }
                    nuevo()
                    onSuccess() // Call the success callback
                }
            }
        }
    }

    // Modified to expose whether save should be prevented
    fun canSave(): Boolean = !shouldPreventSave

    fun nuevo(){
        _uiState.value = Uistate()
    }

    private fun validate(): String? {
        return when {
            _uiState.value.nombre.isBlank() -> "El nombre no puede estar vacío."
            _uiState.value.apellido.isBlank() -> "El apellido no puede estar vacío."
            _uiState.value.genero.isBlank() -> "El género no puede estar vacío."
            _uiState.value.edad == 0 -> "La edad no puede ser cero."
            _uiState.value.pinId.isBlank() -> "Debe asignarle un pin a su hijo."
            else -> null
        }
    }

    private fun getHijos() {
        viewModelScope.launch {
            repository.getAllHijos().collect { hijos ->
                _uiState.update {
                    it.copy(hijos = hijos)
                }
            }
        }
    }

    private fun getPines() {
        viewModelScope.launch {
            pinRepository.getPines().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                pines = resource.data ?: emptyList(),
                                errorMessage = null
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

    private suspend fun validateSelectedHijo(hijo: HijoEntity?): String? {
        return when {
            hijo == null -> "El hijo seleccionado no existe."
            hijo.nombre.isBlank() -> "El nombre no puede estar vacío."
            hijo.apellido.isBlank() -> "El apellido no puede estar vacío."
            hijo.genero.isBlank() -> "El género no puede estar vacío."
            hijo.edad <= 0 -> "La edad debe ser mayor que cero."
            hijo.pinId.isBlank() -> "Debe tener un pin asignado."
            repository.getAllHijos().first().any { existingHijo ->
                existingHijo.nombre.equals(hijo.nombre, ignoreCase = true) &&
                        existingHijo.pinId == hijo.pinId &&
                        existingHijo.hijoId != (hijo.hijoId ?: 0)
            } -> "Ya existe un hijo con este nombre."
            else -> null
        }
    }

    fun onNombreChange(nombre: String) {
        _uiState.update {
            it.copy(nombre = nombre, errorMessage = null)
        }
    }

    fun onApellidoChange(apellido: String) {
        _uiState.update {
            it.copy(apellido = apellido, errorMessage = null)
        }
    }

    fun onGeneroChange(genero: String) {
        _uiState.update {
            it.copy(genero = genero, errorMessage = null)
        }
    }

    fun onEdadChange(edad: Int) {
        _uiState.update {
            it.copy(edad = edad, errorMessage = null)
        }
    }

    fun onPinIdChange(pinId: String) {
        _uiState.update {
            it.copy(pinId = pinId, errorMessage = null)
        }
    }

    fun onUsuarioIdChange(usuarioId: Int) {
        _uiState.update {
            it.copy(usuarioId = usuarioId, errorMessage = null)
        }
    }

    fun deleteHijo() {
        viewModelScope.launch {
            repository.deleteHijo(_uiState.value.toEntity())
        }
    }
}

data class Uistate(
    val hijoId: Int? = null,
    val nombre: String = "",
    val apellido: String = "",
    val genero: String = "",
    val edad: Int = 0,
    val pinId: String= "",
    val usuarioId: Int = 0,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val hijos: List<HijoEntity> = emptyList(),
    val pines: List<PinEntity> = emptyList()
)

fun Uistate.toEntity() = HijoEntity(
    hijoId = hijoId,
    nombre = nombre,
    apellido = apellido,
    genero = genero,
    edad = edad,
    pinId = pinId,
    usuarioId = usuarioId,
)