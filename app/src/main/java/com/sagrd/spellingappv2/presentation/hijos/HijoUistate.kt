package com.sagrd.spellingappv2.presentation.hijos

import com.sagrd.spellingappv2.data.local.entities.HijoEntity
import com.sagrd.spellingappv2.data.local.entities.PinEntity

data class HijoUistate(
    val hijoId: Int? = null,
    val nombre: String = "",
    val errorNombre : String? = null,
    val apellido: String = "",
    val errorApellido : String? = null,
    val genero: String = "",
    val errorGenero : String? = null,
    val edad: Int = 0,
    val errorEdad : String? = null,
    val pinId: String = "",
    val errorPinId : String? = null,
    val usuarioId: Int = 0,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val hijos: List<HijoEntity> = emptyList(),
    val pines: List<PinEntity> = emptyList(),
    val usedPins: Set<String> = emptySet(),
)

fun HijoUistate.toEntity() = HijoEntity(
    hijoId = hijoId,
    nombre = nombre,
    apellido = apellido,
    genero = genero,
    edad = edad,
    pinId = pinId,
    usuarioId = usuarioId,
)