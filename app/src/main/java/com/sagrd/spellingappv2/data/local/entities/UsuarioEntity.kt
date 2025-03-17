package com.sagrd.spellingappv2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Usuarios")
data class UsuarioEntity(
    @PrimaryKey
    val usuarioId: Int? = null,
    val nombre: String = "",
    val apellido: String = "",
    val telefono: String = "",
    val email: String = "",
    val contrasena: String = "",
    val fotoUrl: String = ""
)