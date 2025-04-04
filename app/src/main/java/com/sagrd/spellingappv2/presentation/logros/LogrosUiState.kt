package com.sagrd.spellingappv2.presentation.logros

import com.sagrd.spellingappv2.data.remote.dto.LogrosDto

data class LogrosUiState(
    val logroId: Int = 0,
    val nombreCompleto: String = "",
    val mensaje: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val logros: List<LogrosDto> = emptyList()
)

fun LogrosUiState.toEntity() = LogrosDto(
    logroId = logroId,
    nombreCompleto = nombreCompleto,
    mensaje = mensaje
)