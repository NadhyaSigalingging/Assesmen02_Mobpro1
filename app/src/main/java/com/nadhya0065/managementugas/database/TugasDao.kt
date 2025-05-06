package com.nadhya0065.managementugas.database

import androidx.room.*
import com.nadhya0065.managementugas.model.Tugas
import kotlinx.coroutines.flow.Flow

@Dao
interface TugasDao {
    @Insert
    suspend fun insert(tugas: Tugas)

    @Update
    suspend fun update(tugas: Tugas)

    @Delete
    suspend fun delete(tugas: Tugas)

    @Query("SELECT * FROM tugas WHERE isDeleted = 0 ORDER BY id DESC")
    fun getAll(): Flow<List<Tugas>>

    @Query("SELECT * FROM tugas WHERE isDeleted = 1 ORDER BY id DESC")
    fun getDeleted(): Flow<List<Tugas>>

    @Query("SELECT * FROM tugas WHERE id = :id")
    suspend fun getById(id: Int): Tugas
}
