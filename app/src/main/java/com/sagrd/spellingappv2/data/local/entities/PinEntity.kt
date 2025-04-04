package com.sagrd.spellingappv2.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Pines")
data class PinEntity (
    @PrimaryKey(autoGenerate = true)
    val pinId: Int = 0,
    val pin: String = "",
    @ColumnInfo(defaultValue = "")
    val saludar: String = "",
    @ColumnInfo(defaultValue = "")
    val descripcion: String = ""
)