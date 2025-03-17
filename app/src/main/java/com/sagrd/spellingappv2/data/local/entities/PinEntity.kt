package com.sagrd.spellingappv2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Pines")
class PinEntity (
    @PrimaryKey()
    val pinId: Int? = null,
    val pin: String = "",
)