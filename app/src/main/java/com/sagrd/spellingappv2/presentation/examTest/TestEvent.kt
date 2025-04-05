package com.sagrd.spellingappv2.presentation.examTest

sealed class TestEvent {
    data class OnPlayAudio(val text: String) : TestEvent()
    data class OnPlayDescripcion(val description: String) : TestEvent()
    data object OnNext : TestEvent()
    data object OnPrevious : TestEvent()
}