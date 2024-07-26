package com.sagrd.spellingappv2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Palabras")
data class Palabra(
    @PrimaryKey(autoGenerate = true)
    val palabraId: Int = 0,
    val palabra: String = "",
    val descripcion: String = "",
    val imagenUrl: String = ""
)

