package com.sagrd.spellingappv2.presentation.login

sealed class LoginEvent {
    data class NombreChanged(val nombre: String) : LoginEvent()
    data class ApellidoChanged(val apellido: String) : LoginEvent()
    data class TelefonoChanged(val telefono: String) : LoginEvent()
    data class EmailChanged(val email: String) : LoginEvent()
    data class ContrasenaChanged(val contrasena: String) : LoginEvent()
    data class ConfirmarContrasenaChanged(val confirmarContrasena: String) : LoginEvent()
    data class UpdateUsuario(val password: String) : LoginEvent()
    data class Login(val email: String, val contrasena: String) : LoginEvent()
    object SaveUsuario : LoginEvent()
    object GoogleSignIn : LoginEvent()
}