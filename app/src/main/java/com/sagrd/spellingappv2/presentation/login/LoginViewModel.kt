package com.sagrd.spellingappv2.presentation.login

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.sagrd.spellingappv2.data.local.entities.UsuarioEntity
import com.sagrd.spellingappv2.data.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private suspend fun isPhoneNumberUnique(
        phoneNumber: String,
        currentUserId: Int? = null,
    ): Boolean {
        return usuarioRepository.getAllUsuarios()
            .none { it.telefono == phoneNumber && it.usuarioId != currentUserId }
    }

    fun saveUsuario() {
        viewModelScope.launch {
            if (_uiState.value.nombre.isBlank() || _uiState.value.email.isBlank() || _uiState.value.contrasena.isBlank()) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Todos los campos son obligatorios.",
                        successMessage = null
                    )
                }
                return@launch
            }

            if (_uiState.value.contrasena != _uiState.value.confirmarContrasena) {
                _uiState.update {
                    it.copy(errorMessage = "Las contraseñas no coinciden.", successMessage = null)
                }
                return@launch
            }

            if (!isPhoneNumberUnique(_uiState.value.telefono)) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Este número de teléfono ya está registrado.",
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
                _uiState.update { it.copy(errorMessage = "Nombre no puede estar vacío.") }
                return@launch
            }

            val localUser = usuarioRepository.getUsuarioById(_uiState.value.usuarioId ?: 0)
            val currentLocalPassword = localUser?.contrasena ?: ""
            val isChangingPassword = _uiState.value.contrasena.isNotBlank()
            val firebaseUser = firebaseAuth.currentUser

            if (isChangingPassword) {
                when {
                    currentPassword.isNullOrBlank() -> {
                        _uiState.update { it.copy(errorMessage = "Debe ingresar su contraseña actual") }
                        return@launch
                    }
                    _uiState.value.contrasena != _uiState.value.confirmarContrasena -> {
                        _uiState.update { it.copy(errorMessage = "Las nuevas contraseñas no coinciden") }
                        return@launch
                    }
                    _uiState.value.contrasena.length < 8 -> {
                        _uiState.update { it.copy(errorMessage = "La contraseña debe tener mínimo 8 caracteres") }
                        return@launch
                    }
                    _uiState.value.contrasena == currentLocalPassword -> {
                        _uiState.update { it.copy(errorMessage = "La nueva contraseña no puede ser igual a la actual") }
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
                            it.copy(errorMessage = "Contraseña actual incorrecta: ${e.message}")
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
                        errorMessage = null
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
                apellido = "",
                telefono = "",
                email = "",
                contrasena = "",
                confirmarContrasena = "",
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
                                apellido = localUser.apellido,
                                telefono = localUser.telefono,
                                email = localUser.email,
                                contrasena = "",
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

    fun onNombreChange(nombre: String) {
        _uiState.update {
            it.copy(nombre = nombre)
        }
    }

    fun onApellidoChange(apellido: String) {
        _uiState.update {
            it.copy(apellido = apellido)
        }
    }

    fun onTelefonoChange(telefono: String) {
        _uiState.update {
            it.copy(telefono = telefono)
        }
    }

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun onContrasenaChange(contrasena: String) {
        _uiState.update {
            it.copy(contrasena = contrasena)
        }
    }

    fun onConfirmarContrasenaChange(confirmarContrasena: String) {
        _uiState.update {
            it.copy(confirmarContrasena = confirmarContrasena)
        }
    }

    fun handleGoogleSignInResult(data: Intent?) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)

                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                val authResult = firebaseAuth.signInWithCredential(credential).await()
                val firebaseUser = authResult.user

                if (firebaseUser != null) {
                    Log.d("GoogleSignIn", "Firebase user authenticated: ${firebaseUser.email}")

                    val localUser = usuarioRepository.getAllUsuarios()
                        .find { it.email == firebaseUser.email }

                    if (localUser != null) {
                        Log.d("GoogleSignIn", "Found existing user in local DB")

                        _uiState.update {
                            it.copy(
                                usuarioActual = localUser,
                                firebaseUser = firebaseUser,
                                successMessage = "Inicio de sesión con Google exitoso",
                                errorMessage = null,
                                isLoading = false
                            )
                        }

                        Log.d("GoogleSignIn", "User found in local DB: ${localUser.toString()}")
                    } else {
                        Log.d("GoogleSignIn", "Creating new user in local DB")

                        val displayName = firebaseUser.displayName ?: ""
                        val nameParts = displayName.split(" ")
                        val firstName = if (nameParts.isNotEmpty()) nameParts[0] else ""
                        val lastName = if (nameParts.size > 1) nameParts.subList(1, nameParts.size)
                            .joinToString(" ") else ""

                        val newUser = UsuarioEntity(
                            usuarioId = null,
                            nombre = firstName,
                            apellido = lastName,
                            telefono = firebaseUser.phoneNumber ?: "",
                            email = firebaseUser.email ?: "",
                            contrasena = ""
                        )

                        try {
                            usuarioRepository.insertUsuario(newUser)

                            val insertedUser = usuarioRepository.getAllUsuarios()
                                .find { it.email == firebaseUser.email }

                            if (insertedUser != null) {
                                Log.d(
                                    "GoogleSignIn",
                                    "Successfully created new user with ID: ${insertedUser.usuarioId}"
                                )

                                _uiState.update {
                                    it.copy(
                                        usuarioActual = insertedUser,
                                        firebaseUser = firebaseUser,
                                        successMessage = "Registro con Google exitoso",
                                        errorMessage = null,
                                        isLoading = false
                                    )
                                }
                            } else {
                                Log.e("GoogleSignIn", "Failed to retrieve newly created user")
                                throw Exception("Error al crear usuario en la base de datos local")
                            }
                        } catch (e: Exception) {
                            Log.e("GoogleSignIn", "Error inserting user: ${e.message}", e)
                            throw e
                        }
                    }
                } else {
                    throw Exception("No se pudo autenticar con Firebase")
                }
            } catch (e: Exception) {
                Log.e("GoogleSignIn", "Error in Google sign-in process: ${e.message}", e)
                _uiState.update {
                    it.copy(
                        errorMessage = "Error al iniciar sesión con Google: ${e.message}",
                        successMessage = null,
                        isLoading = false
                    )
                }
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