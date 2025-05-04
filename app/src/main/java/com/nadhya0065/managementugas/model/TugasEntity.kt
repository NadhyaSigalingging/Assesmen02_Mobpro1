package com.nadhya0065.managementugas.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tugas")
data class TugasEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val judul: String,
    val deskripsi: String,
    val tanggal: String,
    val kategori: String,
    val prioritas: String,
    val selesai: Boolean = false,
    val dihapus: Boolean = false // Untuk fitur Recycle Bin
)
