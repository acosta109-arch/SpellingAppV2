package com.sagrd.spellingappv2.presentation.estadisticas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagrd.spellingappv2.data.repository.HijoRepository
import com.sagrd.spellingappv2.data.repository.LogroRepository
import com.sagrd.spellingappv2.data.repository.PalabraRepository
import com.sagrd.spellingappv2.data.repository.PinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EstadisticasViewModel @Inject constructor(
    private val hijoRepository: HijoRepository,
    private val pinesRepository: PinRepository,
    private val palabrasRepository: PalabraRepository,
    private val logrosRepository: LogroRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EstadisticaUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val hijosCount = hijoRepository.getHijosCount()
                val pinesCount = pinesRepository.getPinesCount()
                val palabrasCount = palabrasRepository.getPalabrasCount()
                val logrosCount = logrosRepository.getLogrosCount()

                _uiState.update {
                    it.copy(
                        hijosCount = hijosCount,
                        pinesCount = pinesCount,
                        palabrasCount = palabrasCount,
                        logrosCount = logrosCount,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al cargar estadísticas: ${e.message}"
                    )
                }
            }
        }
    }

    fun refreshStatistics() {
        loadStatistics()
    }
}

