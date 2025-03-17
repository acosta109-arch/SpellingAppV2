package com.sagrd.spellingappv2.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sagrd.spellingappv2.data.local.dao.HijoDao
import com.sagrd.spellingappv2.data.local.dao.UsuarioDao
import com.sagrd.spellingappv2.data.local.entities.HijoEntity
import com.sagrd.spellingappv2.data.local.entities.PalabraEntity
import com.sagrd.spellingappv2.data.local.entities.PinEntity
import com.sagrd.spellingappv2.data.local.entities.UsuarioEntity

@Database(
    entities = [HijoEntity::class,
    PalabraEntity::class,
    PinEntity::class,
    UsuarioEntity::class],
    version = 1,
    exportSchema = false
)

abstract class SpellingAppDb: RoomDatabase(){
    abstract fun hijoDao(): HijoDao
    abstract fun usuarioDao(): UsuarioDao
}