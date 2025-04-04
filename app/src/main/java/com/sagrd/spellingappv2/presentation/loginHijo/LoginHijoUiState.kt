package com.sagrd.spellingappv2.presentation.loginHijo

import com.sagrd.spellingappv2.data.local.entities.PinEntity

data class LoginHijoUiState(
    val pin: String = "",
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isLoginSuccessful: Boolean = false,
    val registeredPins: List<PinEntity> = emptyList(),
    val isLoading: Boolean = false
)