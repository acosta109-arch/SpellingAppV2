package com.sagrd.spellingappv2.data.local.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.sagrd.spellingappv2.data.local.dao.HijoDao
import com.sagrd.spellingappv2.data.local.dao.PalabraDao
import com.sagrd.spellingappv2.data.local.dao.PinDao
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
    version = 5,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
    ]
)

abstract class SpellingAppDb: RoomDatabase(){
    abstract fun hijoDao(): HijoDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun palabraDao(): PalabraDao
    abstract fun pinDao(): PinDao

    companion object
}