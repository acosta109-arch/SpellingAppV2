package com.sagrd.spellingappv2.presentation.loginHijo

sealed class SoyHijoEvent {
    data class OnPinChange(val pin: String) : SoyHijoEvent()
    data class Login(val pin: String) : SoyHijoEvent()
}