package com.nadhya0065.managementugas.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nadhya0065.managementugas.model.Tugas

@Database(entities = [Tugas::class], version = 1, exportSchema = false)
abstract class TugasDatabase : RoomDatabase() {
    abstract fun tugasDao(): TugasDao

    companion object {
        @Volatile
        private var INSTANCE: TugasDatabase? = null

        fun getDatabase(context: Context): TugasDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    TugasDatabase::class.java,
                    "tugas_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
