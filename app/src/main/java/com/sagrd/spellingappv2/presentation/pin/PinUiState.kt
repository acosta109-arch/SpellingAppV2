package com.sagrd.spellingappv2.presentation.pin

import com.sagrd.spellingappv2.data.local.entities.PinEntity

data class PinUiState(
    val pinId: Int? = null,
    val pin: String = "",
    val errorPin: String? = null,
    val utilizado: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val pins: List<PinEntity> = emptyList(),
    val isLoading: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val canDelete: Boolean = false,
    val hijoUsingPin: String? = null
)