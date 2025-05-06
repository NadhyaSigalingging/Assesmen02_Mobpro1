package com.nadhya0065.managementugas.ui.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nadhya0065.managementugas.database.TugasDatabase
import com.nadhya0065.managementugas.model.Tugas
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = TugasDatabase.getDatabase(application).tugasDao()

    val semuaTugas: StateFlow<List<Tugas>> = dao.getAllTugas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun tambahTugas(tugas: Tugas) = viewModelScope.launch {
        dao.insert(tugas)
    }

    fun hapusTugas(tugas: Tugas) = viewModelScope.launch {
        dao.delete(tugas)
    }

    fun updateTugas(tugas: Tugas) = viewModelScope.launch {
        dao.update(tugas)
    }

    suspend fun getTugasById(id: Int): Tugas? = dao.getTugasById(id)
}
