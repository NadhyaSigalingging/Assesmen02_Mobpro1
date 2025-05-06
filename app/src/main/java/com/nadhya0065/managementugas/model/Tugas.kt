package com.nadhya0065.managementugas.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Tugas(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val judul: String,
    val deskripsi: String,
    val deadline: Long,
    val isDeleted: Boolean = false
) {
    fun getPrioritas(): String {
        val sekarang = Calendar.getInstance().timeInMillis
        val beda = deadline - sekarang
        return if (beda < 3 * 24 * 60 * 60 * 1000) "Segera" else "Nanti"
    }
}
