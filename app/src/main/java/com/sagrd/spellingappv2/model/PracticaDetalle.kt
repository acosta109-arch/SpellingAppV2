package com.sagrd.spellingappv2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PracticasDetalle")
data class PracticaDetalle (
    @PrimaryKey(autoGenerate = true)
    val detalleId : Int = 0,
    val palabraId : Int = 0,
    val vecesPracticado : Int  = 0,
    val practicaId : Int = 0
)