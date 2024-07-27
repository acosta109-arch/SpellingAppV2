package com.sagrd.spellingappv2.data.dao

import androidx.room.*
import com.sagrd.spellingappv2.model.Palabra
import kotlinx.coroutines.flow.Flow

@Dao
interface PalabraDao {
    @Upsert()
    suspend fun upsert(palabra: Palabra)

    @Delete
    suspend fun delete(palabra: Palabra)

    @Query("""
        SELECT * FROM Palabras WHERE palabraId=:palabraId 
    """)
    fun find(palabraId: Int): Flow<Palabra>

    @Query("""
        SELECT * 
        FROM Palabras
        ORDER BY palabraId    
    """)
    fun getListStream(): Flow<List<Palabra>>
}