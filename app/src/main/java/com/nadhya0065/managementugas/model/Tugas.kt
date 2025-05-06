package com.nadhya0065.managementugas.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tugas(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val judul: String,
    val deskripsi: String,
    val tanggalDeadline: Long,
    val isPrioritas: Boolean,
    val isDeleted: Boolean = false
)
