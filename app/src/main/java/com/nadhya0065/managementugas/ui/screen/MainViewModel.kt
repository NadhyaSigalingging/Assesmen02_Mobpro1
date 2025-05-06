package com.nadhya0065.managementugas.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nadhya0065.managementugas.database.TugasDao
import com.nadhya0065.managementugas.model.Tugas
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(dao: TugasDao) : ViewModel() {
    val data: StateFlow<List<Tugas>> = dao.getTugas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
