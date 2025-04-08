package com.sagrd.spellingappv2.presentation.login

import com.google.firebase.auth.FirebaseUser
import com.sagrd.spellingappv2.data.local.entities.UsuarioEntity

data class LoginUiState(
    val usuarioId: Int? = null,
    val nombre: String = "",
    val errorNombre: String? = null,
    val apellido: String = "",
    val errorApellido: String? = null,
    val telefono: String = "",
    val errorTelefono: String? = null,
    val email: String = "",
    val errorEmail: String? = null,
    val contrasena: String = "",
    val errorContrasena: String? = null,
    var confirmarContrasena: String = "",
    val errorConfirmarContrasena: String? = null,
    val fotoUrl: String = "",
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val usuarios: List<UsuarioEntity> = emptyList(),
    val usuarioActual: UsuarioEntity? = null,
    val firebaseUser: FirebaseUser? = null,
    val isLoading: Boolean = false,
)

fun LoginUiState.toEntity() = UsuarioEntity(
    usuarioId = usuarioId,
    nombre = nombre,
    apellido = apellido,
    telefono = telefono,
    email = email,
    contrasena = contrasena
)