package com.sagrd.spellingappv2.presentation.palabras

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.sagrd.spellingappv2.data.local.entities.PalabraEntity
import com.sagrd.spellingappv2.data.remote.Resource
import com.sagrd.spellingappv2.data.remote.dto.PalabrasDto
import com.sagrd.spellingappv2.data.repository.PalabraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PalabrasViewModel @Inject constructor(
    private val palabrasRepository: PalabraRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PalabrasUiState())
    val uiState = _uiState.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    init {
        getPalabras()
    }

    fun save() {
        viewModelScope.launch {
            if (isValid()) {
                palabrasRepository.save(_uiState.value.toEntity())
            }
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch {
            palabrasRepository.delete(id)
        }
    }

    fun update() {
        viewModelScope.launch {
            palabrasRepository.update(
                _uiState.value.palabraId, PalabrasDto(
                    palabraId = _uiState.value.palabraId,
                    nombre = _uiState.value.nombre,
                    descripcion = _uiState.value.descripcion,
                    fotoUrl = _uiState.value.fotoUrl
                )
            )
        }
    }

    fun new() {
        _uiState.value = PalabrasUiState()
    }

    fun onNombreChange(nombre: String) {
        _uiState.update {
            it.copy(
                nombre = nombre,
                errorMessage = if (nombre.isBlank()) "Debes rellenar el campo Nombre" else null
            )
        }
    }

    fun onDescripcionChange(descripcion: String) {
        _uiState.update { it.copy(descripcion = descripcion) }
    }

    fun onFotoUrlChange(fotoUrl: String) {
        _uiState.update {
            it.copy(
                fotoUrl = fotoUrl,
                errorMessage = if (!isValidUrl(fotoUrl)) "URL de imagen no válida" else null
            )
        }
    }


    fun getPalabras() {
        viewModelScope.launch {
            palabrasRepository.getPalabras().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                palabras = result.data ?: emptyList(),
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

    fun filterPalabras(query: String) {
        viewModelScope.launch {
            // Get all palabras first
            palabrasRepository.getPalabras().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        // Filter the palabras based on the query
                        val filteredPalabras = if (query.isBlank()) {
                            result.data ?: emptyList()
                        } else {
                            result.data?.filter {
                                it.nombre.contains(query, ignoreCase = true) ||
                                        it.descripcion.contains(query, ignoreCase = true)
                            } ?: emptyList()
                        }

                        _uiState.update {
                            it.copy(
                                palabras = filteredPalabras,
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
        return uiState.value.nombre.isNotBlank() &&
                uiState.value.descripcion.isNotBlank() &&
                isValidUrl(uiState.value.fotoUrl)
    }

    private fun isValidUrl(url: String): Boolean {
        return url.matches(Regex("^(https?://).+\\.(jpg|jpeg|png|gif|bmp)$"))
    }
}

fun PalabrasUiState.toEntity() = PalabrasDto(
    palabraId = palabraId,
    nombre = nombre,
    descripcion = descripcion,
    fotoUrl = fotoUrl
)

data class PalabrasUiState(
    val palabraId: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val fotoUrl: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val palabras: List<PalabraEntity> = emptyList()
)
