package com.sagrd.spellingappv2.presentation.aprender

sealed class AprenderEvent {
    data class OnPlayAudio(val text: String) : AprenderEvent()
    data class OnPlayDescripcion(val description: String) : AprenderEvent()
    data object OnNext : AprenderEvent()
    data object OnPrevious : AprenderEvent()
}