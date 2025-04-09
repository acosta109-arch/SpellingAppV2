package com.sagrd.spellingappv2.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sagrd.spellingappv2.data.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository,
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState get() = _uiState.asStateFlow()
    private val _isAuthenticated = MutableStateFlow(firebaseAuth.currentUser != null)

    init {
        Log.d("UsuarioVM", "ViewModel inicializado")
        getUsuarios()

        firebaseAuth.addAuthStateListener { auth ->
            _isAuthenticated.value = auth.currentUser != null
        }

        firebaseAuth.currentUser?.email?.let { email ->
            viewModelScope.launch {
                val user = usuarioRepository.getUsuarioByEmail(email)
                Log.d("UsuarioVM", "Usuario recuperado al init: $user")
                _uiState.update { it.copy(usuarioActual = user) }
            }
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.NombreChanged -> {
                _uiState.update { it.copy(nombre = event.nombre, errorNombre = null) }
            }

            is LoginEvent.ApellidoChanged -> {
                _uiState.update { it.copy(apellido = event.apellido, errorApellido = null) }
            }

            is LoginEvent.TelefonoChanged -> {
                _uiState.update { it.copy(telefono = event.telefono, errorTelefono = null) }
            }

            is LoginEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.email, errorEmail = null) }
            }

            is LoginEvent.ContrasenaChanged -> {
                _uiState.update { it.copy(contrasena = event.contrasena, errorContrasena = null) }
            }

            is LoginEvent.ConfirmarContrasenaChanged -> {
                _uiState.update {
                    it.copy(
                        confirmarContrasena = event.confirmarContrasena,
                        errorConfirmarContrasena = null
                    )
                }
            }

            is LoginEvent.SaveUsuario -> {
                saveUsuario()
            }

            is LoginEvent.UpdateUsuario -> {
                updateUsuario(event.password)
            }

            is LoginEvent.Login -> {
                _uiState.update {
                    it.copy(
                        email = event.email,
                        errorEmail = null,
                        contrasena = event.contrasena,
                        errorContrasena = null
                    )
                }
                login(event.email, event.contrasena)

            }
        }

    }

    fun saveUsuario() {
        viewModelScope.launch {
            if (_uiState.value.nombre.isBlank()) {
                _uiState.update {
                    it.copy(
                        errorNombre = "El nombre no puede estar vacío.",
                        successMessage = null
                    )
                }
                return@launch
            }

            if (_uiState.value.apellido.isBlank()) {
                _uiState.update {
                    it.copy(
                        errorApellido = "el apellido no puede estar vacío.",
                        successMessage = null
                    )
                }
                return@launch
            }

            if (_uiState.value.telefono.isBlank()) {
                _uiState.update {
                    it.copy(
                        errorTelefono = "el teléfono no puede estar vacío.",
                        successMessage = null
                    )
                }
                return@launch
            }

            if (_uiState.value.email.isBlank()) {
                _uiState.update {
                    it.copy(
                        errorEmail = "El email no puede estar vacío.",
                        successMessage = null
                    )
                }
                return@launch
            }

            if (_uiState.value.contrasena.isBlank()) {
                _uiState.update {
                    it.copy(
                        errorContrasena = "La contraseña es obligatoria.",
                        successMessage = null
                    )
                }
                return@launch
            }

            if (_uiState.value.confirmarContrasena.isBlank()) {
                _uiState.update {
                    it.copy(
                        errorConfirmarContrasena = "Debes confirmar la contraseña.",
                        successMessage = null
                    )
                }
                return@launch
            }

            if (_uiState.value.contrasena != _uiState.value.confirmarContrasena) {
                _uiState.update {
                    it.copy(
                        errorContrasena = "Las contraseñas no coinciden.",
                        errorConfirmarContrasena = "Las contraseñas no coinciden.",
                        successMessage = null
                    )
                }
                return@launch
            }

            if (usuarioRepository.isTelefonoRegistrado(_uiState.value.telefono)) {
                _uiState.update {
                    it.copy(
                        errorTelefono = "Este número de teléfono ya está registrado.",
                        successMessage = null
                    )
                }
                return@launch
            }

            try {
                val authResult = firebaseAuth.createUserWithEmailAndPassword(
                    _uiState.value.email,
                    _uiState.value.contrasena,
                ).await()

                usuarioRepository.insertUsuario(_uiState.value.toEntity())

                _uiState.update {
                    it.copy(
                        successMessage = "Usuario registrado correctamente.",
                        errorMessage = null,
                        errorNombre = null,
                        errorApellido = null,
                        errorTelefono = null,
                        errorEmail = null,
                        errorContrasena = null,
                        errorConfirmarContrasena = null,
                        firebaseUser = authResult.user
                    )
                }
                nuevoUsuario()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Error al registrar el usuario: ${e.message}",
                        successMessage = null
                    )
                }
            }
        }
    }

    fun updateUsuario(currentPassword: String? = null) {
        viewModelScope.launch {

            if (_uiState.value.nombre.isBlank()) {
                _uiState.update { it.copy(errorNombre = "Nombre no puede estar vacío.") }
                return@launch
            }

            val localUser = usuarioRepository.getUsuarioById(_uiState.value.usuarioId ?: 0)
            val currentLocalPassword = localUser?.contrasena ?: ""
            val isChangingPassword = _uiState.value.contrasena.isNotBlank()
            val firebaseUser = firebaseAuth.currentUser

            if (isChangingPassword) {
                when {
                    currentPassword.isNullOrBlank() -> {
                        _uiState.update { it.copy(errorContrasena = "Debe ingresar su contraseña actual") }
                        return@launch
                    }

                    _uiState.value.contrasena != _uiState.value.confirmarContrasena -> {
                        _uiState.update { it.copy(errorConfirmarContrasena = "Las nuevas contraseñas no coinciden") }
                        return@launch
                    }

                    _uiState.value.contrasena.length < 8 -> {
                        _uiState.update { it.copy(errorContrasena = "La contraseña debe tener mínimo 8 caracteres") }
                        return@launch
                    }

                    _uiState.value.contrasena == currentLocalPassword -> {
                        _uiState.update { it.copy(errorContrasena = "La nueva contraseña no puede ser igual a la actual") }
                        return@launch
                    }
                }
            }

            try {
                if (isChangingPassword && firebaseUser != null) {
                    try {
                        val credential = EmailAuthProvider.getCredential(
                            firebaseUser.email ?: "",
                            currentPassword ?: ""
                        )
                        firebaseUser.reauthenticate(credential).await()
                        firebaseUser.updatePassword(_uiState.value.contrasena).await()
                    } catch (e: Exception) {
                        _uiState.update {
                            it.copy(errorContrasena = "Contraseña actual incorrecta: ${e.message}")
                        }
                        return@launch
                    }
                }

                val updatedEntity = _uiState.value.toEntity().copy(
                    contrasena = if (isChangingPassword) _uiState.value.contrasena else currentLocalPassword
                )
                usuarioRepository.updateUsuario(updatedEntity)

                _uiState.update {
                    it.copy(
                        successMessage = "Perfil actualizado correctamente",
                        usuarioActual = updatedEntity,
                        errorMessage = null,
                        errorNombre = null,
                        errorApellido = null,
                        errorTelefono = null,
                        errorEmail = null,
                        errorContrasena = null,
                        errorConfirmarContrasena = null,
                    )
                }

            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error: ${e.message}") }
            }
        }
    }

    fun nuevoUsuario() {
        _uiState.update {
            it.copy(
                usuarioId = null,
                nombre = "",
                errorNombre = null,
                apellido = "",
                errorApellido = null,
                telefono = "",
                errorTelefono = null,
                email = "",
                errorEmail = null,
                contrasena = "",
                errorContrasena = null,
                confirmarContrasena = "",
                errorConfirmarContrasena = null,
                fotoUrl = "",
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun selectUsuario(usuarioId: Int) {
        viewModelScope.launch {
            try {
                if (usuarioId > 0) {
                    val usuario = usuarioRepository.getUsuarioById(usuarioId)
                    _uiState.update {
                        it.copy(
                            usuarioId = usuario?.usuarioId,
                            nombre = usuario?.nombre ?: "",
                            apellido = usuario?.apellido ?: "",
                            telefono = usuario?.telefono ?: "",
                            email = usuario?.email ?: "",
                            usuarioActual = usuario
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("SelectUsuario", "Error selecting usuario: ${e.message}", e)
            }

        }
    }

    fun login(email: String, contrasena: String) {
        viewModelScope.launch {

            if (_uiState.value.email.isBlank()) {
                _uiState.update {
                    it.copy(
                        errorEmail = "Ingrese su Email",
                        successMessage = null
                    )
                }
                return@launch
            }

            if (_uiState.value.contrasena.isBlank()) {
                _uiState.update {
                    it.copy(
                        errorContrasena = "Ingrese su contraseña.",
                        successMessage = null
                    )
                }
                return@launch
            }
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                val authResult = firebaseAuth.signInWithEmailAndPassword(email, contrasena).await()
                val firebaseUser = authResult.user

                if (firebaseUser != null) {
                    var localUser = usuarioRepository.getUsuarioByEmail(email)

                    if (localUser != null) {
                        _uiState.update {
                            it.copy(
                                usuarioId = localUser.usuarioId,
                                nombre = localUser.nombre,
                                errorNombre = null,
                                apellido = localUser.apellido,
                                errorApellido = null,
                                telefono = localUser.telefono,
                                errorTelefono = null,
                                email = localUser.email,
                                errorEmail = null,
                                contrasena = "",
                                errorContrasena = null,
                                confirmarContrasena = "",
                                errorConfirmarContrasena = null,
                                usuarioActual = localUser,
                                firebaseUser = firebaseUser,
                                successMessage = "Inicio de sesión exitoso",
                                errorMessage = null,
                                isLoading = false,
                            )
                        }
                    }


                    Log.d("Login", "Estado actualizado: ${_uiState.value.usuarioActual}")
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Error al iniciar sesión: ${e.message}",
                        successMessage = null,
                        isLoading = false
                    )
                }
                Log.e("Login", "Error en login", e)
            }
        }
    }

    fun getUsuarios() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(usuarios = usuarioRepository.getAllUsuarios())
            }
        }
    }

}

object AuthManager {
    private val auth = FirebaseAuth.getInstance()

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    val isLoggedIn: Boolean
        get() = currentUser != null

    fun logout() {
        auth.signOut()
    }
}