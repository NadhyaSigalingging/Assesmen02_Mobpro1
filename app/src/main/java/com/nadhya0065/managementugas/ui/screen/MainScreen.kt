package com.nadhya0065.managementugas.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onTugasClick: (Int) -> Unit,
    onAddClick: () -> Unit
) {
    val daftarTugas by viewModel.semuaTugas.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Manajemen Tugas") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Text("+")
            }
        }
    ) { padding ->
        if (daftarTugas.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                Text("Belum ada tugas", modifier = Modifier.align(Alignment.Center))
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(daftarTugas, key = { it.id }) { tugas ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTugasClick(tugas.id) }
                            .padding(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(text = tugas.judul, style = MaterialTheme.typography.titleMedium)
                            Text(text = tugas.deskripsi)
                            Text(text = "Prioritas: ${tugas.getPrioritas()}")
                        }
                    }
                }
            }
        }
    }
}
