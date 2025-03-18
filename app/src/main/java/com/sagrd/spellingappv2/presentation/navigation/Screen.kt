package com.sagrd.spellingappv2.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object LoginScreen: Screen()

    @Serializable
    data object RegistrarScreen: Screen()

    @Serializable
    data object Dashboard: Screen()
}