package com.sagrd.spellingappv2.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.sagrd.spellingappv2.data.local.entities.UsuarioEntity
import com.sagrd.spellingappv2.data.repository.UsuarioRepository
import com.sagrd.spellingappv2.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState get() = _uiState.asStateFlow()
    private var _navController: NavHostController? = null

    init {
        getUsuarios()
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

            try {
                usuarioRepository.insertUsuario(_uiState.value.toEntity())
                _uiState.update {
                    it.copy(successMessage = "Usuario guardado correctamente.", errorMessage = null)
                }
                nuevoUsuario()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Error al guardar el usuario: ${e.message}", successMessage = null)
                }
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
                        fotoUrl = usuario?.fotoUrl ?: ""
                    )
                }
            }
        }
    }

    fun deleteUsuario() {
        viewModelScope.launch {
            try {
                usuarioRepository.deleteUsuario(_uiState.value.toEntity())
                _uiState.update {
                    it.copy(successMessage = "Usuario eliminado correctamente.", errorMessage = null)
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
            val usuario = usuarioRepository.getAllUsuarios()
                .find { it.email == email && it.contrasena == contrasena }

            if (usuario != null) {
                _uiState.update {
                    it.copy(
                        usuarioActual = usuario,
                        successMessage = "Inicio de sesión exitoso",
                        errorMessage = null
                    )
                }
            } else {
                _uiState.update {
                    it.copy(errorMessage = "Email o contraseña incorrectos", successMessage = null)
                }
            }
        }
    }

    fun setNavController(navController: NavHostController) {
        _navController = navController
    }

    fun logout() {
        _uiState.update { currentState ->
            currentState.copy(usuarioActual = null)
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

    fun clearMessages() {
        _uiState.update {
            it.copy(errorMessage = null, successMessage = null)
        }
    }

    data class UiState(
        val usuarioId: Int? = null,
        val nombre: String = "",
        val apellido: String = "",
        val telefono: String = "",
        val email: String = "",
        val contrasena: String = "",
        val confirmarContrasena: String = "",
        val fotoUrl: String = "",
        val errorMessage: String? = null,
        val successMessage: String? = null,
        val usuarios: List<UsuarioEntity> = emptyList(),
        val usuarioActual: UsuarioEntity? = null
    )

    fun UiState.toEntity() = UsuarioEntity(
        usuarioId = usuarioId,
        nombre = nombre,
        apellido = apellido,
        telefono = telefono,
        email = email,
        contrasena = contrasena,
        fotoUrl = fotoUrl
    )
}