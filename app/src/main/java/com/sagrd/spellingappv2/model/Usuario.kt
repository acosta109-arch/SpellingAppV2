package com.sagrd.spellingappv2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Usuarios")
data class Usuario (
    @PrimaryKey(autoGenerate = true)
    val usuarioId : Int = 0,
    val nombres: String,
    val edad : Int
)