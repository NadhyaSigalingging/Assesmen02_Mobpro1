package com.nadhya0065.managementugas.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tugas")
data class Tugas(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val judul: String,
    val deskripsi: String,
    val prioritas: String,
    val deadline: String,
    val isDeleted: Boolean = false
)
