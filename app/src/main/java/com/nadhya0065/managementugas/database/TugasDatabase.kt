package com.nadhya0065.managementugas.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nadhya0065.managementugas.model.Tugas

@Database(entities = [Tugas::class], version = 1, exportSchema = false)
abstract class TugasDatabase : RoomDatabase() {

    abstract val dao : TugasDao

    companion object {

        @Volatile
        private var INSTANCE: TugasDatabase? = null

        fun getInstance(context: Context): TugasDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TugasDatabase::class.java,
                        "tugas.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}