package com.example.spellingappv2.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Practicas")
data class Practica (
    @PrimaryKey(autoGenerate = true)
    val practicaId : Int = 0,/*
    val fecha : Date = Calendar.getInstance().getTime()*/
    val usuarioId : Int = 0,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val fecha : String,
    val fraseId : Int = 0,
    val vecesPracticado : Int = 0
)