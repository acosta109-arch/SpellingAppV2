package com.sagrd.spellingappv2.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object LoginScreen: Screen()

    @Serializable
    data object RegistrarScreen: Screen()

    @Serializable
    data object Dashboard: Screen()

    @Serializable
    data class PinScreen(val pinId: Int): Screen()

    @Serializable
    data class PinDelete(val pinId: Int): Screen()

    @Serializable
    data object PinListScreen: Screen()

    @Serializable
    data class HijoScreen(val hijoId: Int): Screen()

    @Serializable
    data class HijoDelete(val hijoId: Int): Screen()

    @Serializable
    data class HijoEdit(val hijoId: Int): Screen()

    @Serializable
    data object HijoListScreen: Screen()

    @Serializable
    data class Perfil(val usuarioId: Int): Screen()

    @Serializable
    data class EditPerfil(val UsuarioId: Int): Screen()

    @Serializable
    data object PalabrasListScreen: Screen()


}