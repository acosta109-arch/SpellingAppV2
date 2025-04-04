package com.sagrd.spellingappv2.presentation.estadisticas

data class EstadisticaUiState(
    val hijosCount: Int = 0,
    val pinesCount: Int = 0,
    val palabrasCount: Int = 0,
    val logrosCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)