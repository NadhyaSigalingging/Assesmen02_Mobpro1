package com.nadhya0065.managementugas.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nadhya0065.managementugas.database.TugasDao
import com.nadhya0065.managementugas.model.Tugas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val dao: TugasDao) : ViewModel() {

    var recentlyDeletedTugas: Tugas? = null

    suspend fun getTugas(id: Long): Tugas? {
        return dao.getTugasById(id)
    }

    fun insert(nama_tugas: String, deskripsi: String, deadline: String, prioritas: String) {
        val tugas = Tugas(
            nama_tugas = nama_tugas,
            dekripsi = deskripsi,
            deadline = deadline,
            prioritas = prioritas,
            isDeleted = false
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(tugas)
        }
    }

    fun update(id: Long, nama_tugas: String, deskripsi: String, prioritas: String, deadline: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val current = dao.getTugasById(id)
            if (current != null) {
                val tugas = Tugas(
                    id = id,
                    nama_tugas = nama_tugas,
                    dekripsi = deskripsi,
                    prioritas = prioritas,
                    deadline = deadline,
                    isDeleted = current.isDeleted
                )
                dao.update(tugas)
            }
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.softDeleteById(id)
        }
    }

    fun restoreDeletedTugas() {
        viewModelScope.launch(Dispatchers.IO) {
            recentlyDeletedTugas?.let {
                dao.update(it.copy(isDeleted = false))
                recentlyDeletedTugas = null
            }
        }
    }
}
