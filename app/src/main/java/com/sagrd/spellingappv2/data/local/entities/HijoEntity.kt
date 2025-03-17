package com.sagrd.spellingappv2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Hijos")
data class HijoEntity(
    @PrimaryKey
    val hijoId: Int? = null,
    val nombre: String = "",
    val apellido: String = "",
    val genero: String = "",
    val edad: Int,
    val pinId: String,
    val usuarioId: Int
)