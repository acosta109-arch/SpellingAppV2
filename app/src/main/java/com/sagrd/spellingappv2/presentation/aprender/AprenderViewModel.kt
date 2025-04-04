package com.sagrd.spellingappv2.presentation.aprender

import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagrd.spellingappv2.data.local.entities.PalabraEntity
import com.sagrd.spellingappv2.data.remote.Resource
import com.sagrd.spellingappv2.data.repository.PalabraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject



@HiltViewModel
class AprenderViewModel @Inject constructor(
    private val palabraRepository: PalabraRepository,
    private val textToSpeech: TextToSpeech
) : ViewModel() {

    private val _uiState = MutableStateFlow(AprenderUiState())
    val uiState: StateFlow<AprenderUiState> = _uiState.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    init {
        getPalabras()
        initTextToSpeech()
    }

    private fun initTextToSpeech() {
        val locEnglish = Locale.US
        if (TextToSpeech.LANG_AVAILABLE == textToSpeech.isLanguageAvailable(locEnglish)) {
            textToSpeech.language = locEnglish
        }
        textToSpeech.setSpeechRate(0.8f)
        textToSpeech.setPitch(1.0f)
    }


    fun playDescripcion(description: String) {
        textToSpeech?.let { tts ->
            tts.language = Locale.ENGLISH
            tts.speak(description, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun getPalabras() {
        viewModelScope.launch {
            palabraRepository.getPalabras().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                palabras = result.data ?: emptyList(),
                                totalPalabras = (result.data?.size ?: 0),
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

    fun playAudio(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "palabra_id")
    }

    fun nextPalabra() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val newIndex = (currentState.palabraActual + 1).coerceAtMost(currentState.totalPalabras - 1)
                val newPercentage = if (currentState.totalPalabras > 1) {
                    newIndex.toFloat() / (currentState.totalPalabras - 1)
                } else {
                    1f
                }

                currentState.copy(
                    palabraActual = newIndex,
                    porcentajeCompletado = newPercentage
                )
            }
        }
    }

    fun previousPalabra() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val newIndex = (currentState.palabraActual - 1).coerceAtLeast(0)
                val newPercentage = if (currentState.totalPalabras > 1) {
                    newIndex.toFloat() / (currentState.totalPalabras - 1)
                } else {
                    0f
                }

                currentState.copy(
                    palabraActual = newIndex,
                    porcentajeCompletado = newPercentage
                )
            }
        }
    }

    fun setCurrentPalabra(index: Int) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val validIndex = index.coerceIn(0, currentState.totalPalabras - 1)
                val newPercentage = if (currentState.totalPalabras > 1) {
                    validIndex.toFloat() / (currentState.totalPalabras - 1)
                } else {
                    if (validIndex > 0) 1f else 0f
                }

                currentState.copy(
                    palabraActual = validIndex,
                    porcentajeCompletado = newPercentage
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}