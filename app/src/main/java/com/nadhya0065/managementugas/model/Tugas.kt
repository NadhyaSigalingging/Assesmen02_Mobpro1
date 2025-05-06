package com.nadhya0065.managementugas.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tugas")
data class Tugas(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nama_tugas: String,
    val dekripsi: String,
    val prioritas: String
)
