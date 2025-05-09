package com.nadhya0065.managementugas.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nadhya0065.managementugas.model.Tugas
import kotlinx.coroutines.flow.Flow

@Dao
interface TugasDao {

    @Insert
    suspend fun insert(tugas: Tugas)

    @Update
    suspend fun update(tugas: Tugas)

    @Query("SELECT * FROM tugas WHERE isDeleted = 0 ORDER BY prioritas ASC")
    fun getTugas(): Flow<List<Tugas>>

    @Query("SELECT * FROM tugas WHERE isDeleted = 1 ORDER BY prioritas ASC")
    fun getDeletedTugas(): Flow<List<Tugas>>

    @Query("SELECT * FROM tugas WHERE id = :id")
    suspend fun getTugasById(id: Long): Tugas?

    @Query("UPDATE tugas SET isDeleted = 1 WHERE id = :id")
    suspend fun softDeleteById(id: Long)

    @Query("UPDATE tugas SET isDeleted = 0 WHERE id = :id")
    suspend fun restoreById(id: Long)

    @Query("DELETE FROM tugas WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM tugas WHERE isDeleted = 1")
    suspend fun emptyRecycleBin()
}
