package com.nadhya0065.managementugas.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nadhya0065.managementugas.model.Tugas

@Database(entities = [Tugas::class], version = 1)
abstract class TugasDatabase : RoomDatabase() {
    abstract fun tugasDao(): TugasDao
}
