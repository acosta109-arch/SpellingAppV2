package com.sagrd.spellingappv2.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class UsuarioConHijoEntity(
    @Embedded val usuario: UsuarioEntity,
    @Relation(
        parentColumn = "usuarioId",
        entityColumn = "usuarioId"
    )
    val hijos: List<HijoEntity>
)