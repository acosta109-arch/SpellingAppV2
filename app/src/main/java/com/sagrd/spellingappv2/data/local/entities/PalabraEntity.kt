package com.sagrd.spellingappv2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "palabras")
class PalabraEntity(
    @PrimaryKey()
    val palabraId: Int? = null,
    val palabra: String = "",
    val descripcion: String = "",
    val fotoUrl: String = "",
)