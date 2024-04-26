package com.example.spellingappv2.data.dao

import androidx.room.*
import com.example.spellingappv2.model.Palabra
import kotlinx.coroutines.flow.Flow

@Dao
interface PalabraDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(palabra: Palabra)

    @Delete
    suspend fun eliminar(palabra: Palabra)

    @Query("""
        SELECT * FROM Palabras WHERE palabraId=:palabraId 
    """)
    fun buscar(palabraId: Int): Flow<Palabra>

    @Query("""
        SELECT * 
        FROM Palabras
        ORDER BY palabraId    
    """)
    fun getList(): Flow<List<Palabra>>
}