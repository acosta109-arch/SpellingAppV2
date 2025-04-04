package com.sagrd.spellingappv2.presentation.pin

sealed class PinEvent{
    data class OnPinChange(val dato: String): PinEvent()
    data object OnSave: PinEvent()
    data object OnDelete: PinEvent()
    data object OnHideDialog: PinEvent()
    data object CheckPinUsage: PinEvent()
}