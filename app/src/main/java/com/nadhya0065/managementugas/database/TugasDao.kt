package com.nadhya0065.managementugas.database

import androidx.room.Dao
import androidx.room.Delete
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

    @Delete
    suspend fun delete(tugas: Tugas)

    @Query("SELECT * FROM tugas ORDER BY prioritas DESC")
    fun getTugas(): Flow<List<Tugas>>

    @Query("SELECT * FROM tugas WHERE id = :id")
    suspend fun getTugasById(id: Long): Tugas?

    @Query("DELETE FROM tugas WHERE id = :id")
    suspend fun deleteById(id:Long)
}