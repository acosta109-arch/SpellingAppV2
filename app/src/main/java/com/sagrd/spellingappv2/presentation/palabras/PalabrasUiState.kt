package com.sagrd.spellingappv2.presentation.palabras

import com.sagrd.spellingappv2.data.local.entities.PalabraEntity
import com.sagrd.spellingappv2.data.remote.dto.PalabrasDto

data class PalabrasUiState(
    val palabraId: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val fotoUrl: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val palabras: List<PalabraEntity> = emptyList()
)

fun PalabrasUiState.toEntity() = PalabrasDto(
    palabraId = palabraId,
    nombre = nombre,
    descripcion = descripcion,
    fotoUrl = fotoUrl
)