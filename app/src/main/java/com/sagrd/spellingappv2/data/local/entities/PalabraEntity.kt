package com.sagrd.spellingappv2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Palabras")
data class PalabraEntity(
    @PrimaryKey
    val palabraId: Int? = null,
    val nombre: String = "",
    val descripcion: String = "",
    val fotoUrl: String = "",
)