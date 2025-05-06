package com.nadhya0065.managementugas.database

import androidx.room.*
import com.nadhya0065.managementugas.model.Tugas
import kotlinx.coroutines.flow.Flow

@Dao
interface TugasDao {
    @Query("SELECT * FROM tugas WHERE isDeleted = 0 ORDER BY deadline ASC")
    fun getAllTugas(): Flow<List<Tugas>>

    @Query("SELECT * FROM tugas WHERE id = :id")
    suspend fun getTugasById(id: Int): Tugas?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tugas: Tugas)

    @Update
    suspend fun update(tugas: Tugas)

    @Delete
    suspend fun delete(tugas: Tugas)
}
