package com.sagrd.spellingappv2.presentation.hijos

import com.sagrd.spellingappv2.data.local.entities.HijoEntity
import com.sagrd.spellingappv2.data.local.entities.PinEntity

data class Uistate(
    val hijoId: Int? = null,
    val nombre: String = "",
    val apellido: String = "",
    val genero: String = "",
    val edad: Int = 0,
    val pinId: String = "",
    val usuarioId: Int = 0,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val hijos: List<HijoEntity> = emptyList(),
    val pines: List<PinEntity> = emptyList(),
    val usedPins: Set<String> = emptySet(),
)

fun Uistate.toEntity() = HijoEntity(
    hijoId = hijoId,
    nombre = nombre,
    apellido = apellido,
    genero = genero,
    edad = edad,
    pinId = pinId,
    usuarioId = usuarioId,
)