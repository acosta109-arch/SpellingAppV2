package com.sagrd.spellingappv2.presentation.login

import android.content.Intent
import android.util.Log
import androidx.compose.ui.text.input.OffsetMapping
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.sagrd.spellingappv2.data.local.entities.UsuarioEntity
import com.sagrd.spellingappv2.data.repository.UsuarioRepository
import com.sagrd.spellingappv2.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState get() = _uiState.asStateFlow()
    private var _navController: NavHostController? = null

    init {
        getUsuarios()
    }

    private suspend fun isPhoneNumberUnique(phoneNumber: String, currentUserId: Int? = null): Boolean {
        return usuarioRepository.getAllUsuarios()
            .none { it.telefono == phoneNumber && it.usuarioId != currentUserId }
    }

    fun saveUsuario() {
        viewModelScope.launch {
            if (_uiState.value.nombre.isBlank() || _uiState.value.email.isBlank() || _uiState.value.contrasena.isBlank()) {
                _uiState.update {
                    it.copy(errorMessage = "Todos los campos son obligatorios.", successMessage = null)
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
                    it.copy(errorMessage = "Error al registrar el usuario: ${e.message}", successMessage = null)
                }
            }
        }
    }

    fun updateUsuario() {
        viewModelScope.launch {
            if (_uiState.value.nombre.isBlank() || _uiState.value.email.isBlank()) {
                _uiState.update {
                    it.copy(errorMessage = "Nombre y email son obligatorios.", successMessage = null)
                }
                return@launch
            }

            if (_uiState.value.firebaseUser == null && _uiState.value.contrasena.isBlank()) {
                _uiState.update {
                    it.copy(errorMessage = "La contraseña es obligatoria.", successMessage = null)
                }
                return@launch
            }

            if (_uiState.value.firebaseUser == null && _uiState.value.contrasena != _uiState.value.confirmarContrasena) {
                _uiState.update {
                    it.copy(errorMessage = "Las contraseñas no coinciden.", successMessage = null)
                }
                return@launch
            }

            if (!isPhoneNumberUnique(_uiState.value.telefono, _uiState.value.usuarioId)) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Este número de teléfono ya está registrado por otro usuario.",
                        successMessage = null
                    )
                }
                return@launch
            }

            try {
                if (_uiState.value.firebaseUser != null && _uiState.value.contrasena.isNotBlank()) {
                    firebaseAuth.currentUser?.updatePassword(_uiState.value.contrasena)?.await()

                    val db = FirebaseFirestore.getInstance()
                    val userId = firebaseAuth.currentUser?.uid

                    if (userId != null) {
                        val userUpdates = hashMapOf<String, Any>(
                            "nombre" to _uiState.value.nombre,
                            "apellido" to _uiState.value.apellido,
                            "telefono" to _uiState.value.telefono,
                            "email" to _uiState.value.email
                        )

                        if (_uiState.value.contrasena.isNotBlank()) {
                            userUpdates["contrasena"] = _uiState.value.contrasena
                        }

                        db.collection("usuarios").document(userId)
                            .update(userUpdates)
                            .await()
                    }
                }

                usuarioRepository.updateUsuario(_uiState.value.toEntity())

                _uiState.update {
                    it.copy(
                        successMessage = "Usuario actualizado correctamente.",
                        errorMessage = null
                    )
                }
                nuevoUsuario()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Error al actualizar el usuario: ${e.message}",
                        successMessage = null
                    )
                }
                Log.e("UpdateUsuario", "Error updating user: ${e.message}", e)
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
            if (usuarioId > 0) {
                val usuario = usuarioRepository.getUsuarioById(usuarioId)
                _uiState.update {
                    it.copy(
                        usuarioId = usuario?.usuarioId,
                        nombre = usuario?.nombre ?: "",
                        apellido = usuario?.apellido ?: "",
                        telefono = usuario?.telefono ?: "",
                        email = usuario?.email ?: "",
                        contrasena = usuario?.contrasena ?: "",
                        usuarioActual = usuario
                    )
                }
            }
        }
    }

    fun deleteUsuario() {
        viewModelScope.launch {
            try {
                _uiState.value.firebaseUser?.let { user ->
                    try {
                        user.delete().await()
                    } catch (e: Exception) {
                        _uiState.update {
                            it.copy(errorMessage = "Error al eliminar usuario de Firebase: ${e.message}", successMessage = null)
                        }
                        return@launch
                    }
                }

                usuarioRepository.deleteUsuario(_uiState.value.toEntity())

                _uiState.update {
                    it.copy(
                        successMessage = "Usuario eliminado correctamente.",
                        errorMessage = null,
                        firebaseUser = null,
                        usuarioActual = null
                    )
                }
                nuevoUsuario()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Error al eliminar el usuario: ${e.message}", successMessage = null)
                }
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
                    val localUser = usuarioRepository.getAllUsuarios()
                        .find { it.email == email }

                    if (localUser != null) {
                        _uiState.update {
                            it.copy(
                                usuarioActual = localUser,
                                firebaseUser = firebaseUser,
                                successMessage = "Inicio de sesión exitoso",
                                errorMessage = null,
                                isLoading = false
                            )
                        }
                    } else {
                        val newUser = UsuarioEntity(
                            usuarioId = null,
                            nombre = firebaseUser.displayName?.split(" ")?.firstOrNull() ?: "",
                            apellido = firebaseUser.displayName?.split(" ")?.lastOrNull() ?: "",
                            telefono = firebaseUser.phoneNumber ?: "",
                            email = firebaseUser.email ?: "",
                            contrasena = ""
                        )

                        usuarioRepository.insertUsuario(newUser)
                        val insertedUser = usuarioRepository.getAllUsuarios()
                            .find { it.email == email }

                        _uiState.update {
                            it.copy(
                                usuarioActual = insertedUser,
                                firebaseUser = firebaseUser,
                                successMessage = "Inicio de sesión exitoso",
                                errorMessage = null,
                                isLoading = false
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Error al iniciar sesión: ${e.message}",
                        successMessage = null,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun logout() {
        firebaseAuth.signOut()

        _uiState.update { currentState ->
            currentState.copy(
                usuarioActual = null,
                firebaseUser = null
            )
        }

        _navController?.navigate(Screen.LoginScreen) {
            popUpTo(0) {
                inclusive = true
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

    fun onFotoUrlChange(fotoUrl: String) {
        _uiState.update {
            it.copy(fotoUrl = fotoUrl)
        }
    }

    data class UiState(
        val usuarioId: Int? = null,
        val nombre: String = "",
        val apellido: String = "",
        val telefono: String = "",
        val email: String = "",
        val contrasena: String = "",
        var confirmarContrasena: String = "",
        val fotoUrl: String = "",
        val errorMessage: String? = null,
        val successMessage: String? = null,
        val usuarios: List<UsuarioEntity> = emptyList(),
        val usuarioActual: UsuarioEntity? = null,
        val firebaseUser: FirebaseUser? = null,
        val isLoading: Boolean = false
    )

    fun UiState.toEntity() = UsuarioEntity(
        usuarioId = usuarioId,
        nombre = nombre,
        apellido = apellido,
        telefono = telefono,
        email = email,
        contrasena = contrasena
    )

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
                    } else {
                        Log.d("GoogleSignIn", "Creating new user in local DB")

                        val displayName = firebaseUser.displayName ?: ""
                        val nameParts = displayName.split(" ")
                        val firstName = if (nameParts.isNotEmpty()) nameParts[0] else ""
                        val lastName = if (nameParts.size > 1) nameParts.subList(1, nameParts.size).joinToString(" ") else ""

                        val newUser = UsuarioEntity(
                            usuarioId = null,  // Let Room generate the ID
                            nombre = firstName,
                            apellido = lastName,
                            telefono = firebaseUser.phoneNumber ?: "",
                            email = firebaseUser.email ?: "",
                            contrasena = ""  // No password needed for Google auth
                        )

                        try {
                            usuarioRepository.insertUsuario(newUser)

                            val insertedUser = usuarioRepository.getAllUsuarios()
                                .find { it.email == firebaseUser.email }

                            if (insertedUser != null) {
                                Log.d("GoogleSignIn", "Successfully created new user with ID: ${insertedUser.usuarioId}")

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

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser

                    checkIfUserExistsInFirestore(user?.uid, account)
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Error de autenticación: ${task.exception?.message}"
                        )
                    }
                }
            }
    }

    private fun checkIfUserExistsInFirestore(userId: String?, account: GoogleSignInAccount) {
        if (userId == null) {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Error: ID de usuario no disponible") }
            return
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("usuarios").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Inicio de sesión exitoso"
                        )
                    }
                } else {
                    createNewUserFromGoogleAccount(userId, account)
                }
            }
            .addOnFailureListener { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al verificar usuario: ${e.message}"
                    )
                }
            }
    }

    private fun createNewUserFromGoogleAccount(userId: String, account: GoogleSignInAccount) {
        val db = FirebaseFirestore.getInstance()

        val userMap = hashMapOf(
            "uid" to userId,
            "nombre" to (account.givenName ?: ""),
            "apellido" to (account.familyName ?: ""),
            "email" to (account.email ?: ""),
            "fotoUrl" to (account.photoUrl?.toString() ?: ""),
            "telefono" to "",
            "authProvider" to "google"
        )

        db.collection("usuarios").document(userId)
            .set(userMap)
            .addOnSuccessListener {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Registro e inicio de sesión exitoso"
                    )
                }
            }
            .addOnFailureListener { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al crear usuario: ${e.message}"
                    )
                }
            }
    }
}