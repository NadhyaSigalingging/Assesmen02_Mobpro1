package com.nadhya0065.managementugas.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nadhya0065.managementugas.model.Tugas
import kotlinx.coroutines.flow.Flow

@Dao
interface TugasDao {
    @Query("SELECT * FROM Tugas WHERE isDeleted = 0 ORDER BY tanggalDeadline ASC")
    fun getAll(): Flow<List<Tugas>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tugas: Tugas)

    @Update
    suspend fun update(tugas: Tugas)

    @Query("UPDATE Tugas SET isDeleted = 1 WHERE id = :id")
    suspend fun softDelete(id: Int)

    @Query("UPDATE Tugas SET isDeleted = 0 WHERE id = :id")
    suspend fun undoDelete(id: Int)
}
