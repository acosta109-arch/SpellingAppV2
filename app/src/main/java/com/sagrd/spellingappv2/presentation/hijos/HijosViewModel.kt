package com.sagrd.spellingappv2.presentation.hijos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagrd.spellingappv2.data.local.entities.HijoEntity
import com.sagrd.spellingappv2.data.local.entities.PinEntity
import com.sagrd.spellingappv2.data.repository.HijoRepository
import com.sagrd.spellingappv2.data.repository.PinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class hijosViewModel @Inject constructor(
    private val repository: HijoRepository,
    private val pinRepository: PinRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(Uistate())
    val uiState = _uiState.asStateFlow()

    init {
        getHijos()
        getPines()
    }

    fun saveHijo() {
        viewModelScope.launch {}
        val validationError = validate()
        if (validationError != null) {
            _uiState.update {
                it.copy(errorMessage = validationError, successMessage = null)
            }
        } else {
            viewModelScope.launch {
                repository.saveHijo(_uiState.value.toEntity())
            }
            nuevo()
        }
    }

    fun nuevo(){
        _uiState.value = Uistate()
    }
    private fun validate(): String? {
        return when {
            _uiState.value.nombre.isBlank() -> "El nombre no puede estar vacío."
            _uiState.value.apellido.isBlank() -> "El apellido no puede estar vacío."
            _uiState.value.genero.isBlank() -> "El genero no puede estar vacío."
            _uiState.value.edad == 0 -> "La edad no puede estar vacío."
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
            pinRepository.getAllPines().collect { pines ->
                _uiState.update {
                    it.copy(pines = pines)
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

    fun onNombreChange(nombre: String) {
        _uiState.update {
            it.copy(nombre = nombre)
        }
    }

    fun onApellidoChange(apellido: String) {
        _uiState.update {
            it.copy(apellido = apellido)
        }
    }

    fun onGeneroChange(genero: String) {
        _uiState.update {
            it.copy(genero = genero)
        }
    }

    fun onEdadChange(edad: Int) {
        _uiState.update {
            it.copy(edad = edad)
        }
    }

    fun onPinIdChange(pinId: String) {
        _uiState.update {
            it.copy(pinId = pinId)
        }
    }

    fun onUsuarioIdChange(usuarioId: Int) {
        _uiState.update {
            it.copy(usuarioId = usuarioId)
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