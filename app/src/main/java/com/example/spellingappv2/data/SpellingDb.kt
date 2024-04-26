package com.example.spellingappv2.data

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import com.example.spellingappv2.data.dao.PalabraDao
import com.example.spellingappv2.data.dao.PracticaDao
import com.example.spellingappv2.data.dao.PracticaDetalleDao
import com.example.spellingappv2.data.dao.UsuarioDao
import com.example.spellingappv2.model.Palabra
import com.example.spellingappv2.model.Practica
import com.example.spellingappv2.model.PracticaDetalle
import com.example.spellingappv2.model.Usuario
import java.util.*

@Database(
    entities = [Usuario::class, Palabra::class, Practica::class, PracticaDetalle::class],
    exportSchema = true,
    version = 2,
    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ]
)
@TypeConverters(Converters::class)
abstract class SpellingDb: RoomDatabase() {
    abstract val usuarioDao: UsuarioDao
    abstract val palabraDao : PalabraDao
    abstract val practicaDao : PracticaDao
    abstract val detalleDao : PracticaDetalleDao

    /*@DeleteColumn(tableName = "Practicas",columnName = "fraseId")
    class MigrateQuizQuesToQuizQuestion : AutoMigrationSpec { }*/
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}