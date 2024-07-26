package com.sagrd.spellingappv2.model

data class SpellListState (
    val isLoading : Boolean = false,
    val usuarios : List<Usuario> = emptyList(),
    val error : String = "",
    val existeUsuarios : Boolean = false,
    val palabras : List<Palabra> = emptyList()
)
