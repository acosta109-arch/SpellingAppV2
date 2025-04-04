package com.sagrd.spellingappv2.presentation.examTest

import com.sagrd.spellingappv2.data.local.entities.PalabraEntity

data class TestUiState(
    val palabraActual: Int = 0,
    val totalPalabras: Int = 0,
    val porcentajeCompletado: Float = 0f,
    val palabras: List<PalabraEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)