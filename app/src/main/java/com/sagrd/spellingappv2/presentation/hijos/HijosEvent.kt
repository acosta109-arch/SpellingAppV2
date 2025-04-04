package com.sagrd.spellingappv2.presentation.hijos

sealed class HijosEvent {
    data class OnNombreChange(val dato: String) : HijosEvent()
    data class OnApellidoChange(val dato: String) : HijosEvent()
    data class OnGeneroChange(val dato: String) : HijosEvent()
    data class OnEdadChange(val dato: String) : HijosEvent()
    data class OnPinChange(val dato: String) : HijosEvent()
    data class OnUsuarioIdChange(val dato: String) : HijosEvent()
    data object OnSave: HijosEvent()
    data object OnDelete: HijosEvent()
}