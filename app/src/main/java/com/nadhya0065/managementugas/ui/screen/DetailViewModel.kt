package com.nadhya0065.managementugas.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nadhya0065.managementugas.database.TugasDao
import com.nadhya0065.managementugas.model.Tugas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val dao: TugasDao) : ViewModel() {

    var recentlyDeletedTugas: Tugas? = null
        private set

    suspend fun getTugas(id: Long): Tugas? {
        return dao.getTugasById(id)
    }

    fun insert(nama_tugas: String, deskripsi: String, prioritas: String) {
        val tugas = Tugas(
            nama_tugas = nama_tugas,
            dekripsi = deskripsi,
            prioritas = prioritas
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(tugas)
        }
    }

    fun update(id: Long, nama_tugas: String, deskripsi: String, prioritas: String) {
        val tugas = Tugas(
            id = id,
            nama_tugas = nama_tugas,
            dekripsi = deskripsi,
            prioritas = prioritas
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(tugas)
        }
    }

    fun delete(tugas: Tugas) {
        viewModelScope.launch(Dispatchers.IO) {
            recentlyDeletedTugas = tugas
            dao.delete(tugas)
        }
    }

    fun restoreDeletedTugas() {
        viewModelScope.launch(Dispatchers.IO) {
            recentlyDeletedTugas?.let { dao.insert(it) }
            recentlyDeletedTugas = null
        }
    }
}
