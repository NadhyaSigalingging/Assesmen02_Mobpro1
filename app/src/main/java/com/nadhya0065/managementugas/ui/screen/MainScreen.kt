package com.nadhya0065.managementugas.ui.screen

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nadhya0065.managementugas.R
import com.nadhya0065.managementugas.model.Tugas
import com.nadhya0065.managementugas.navigation.Screen
import com.nadhya0065.managementugas.util.SettingDataStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel) {
    val context = LocalContext.current
    val settingDataStore = remember { SettingDataStore(context) }
    val scope = rememberCoroutineScope()

    val isGridView by settingDataStore.getViewType.collectAsState(initial = false)
    val tugasList by viewModel.semuaTugas.collectAsState()

    var showDialog by remember { mutableStateOf<Tugas?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(

                title = { Text(text = stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        scope.launch { settingDataStore.saveViewType(!isGridView) }
                    }) {
                        Icon(
                            painter = painterResource(
                                id = if (isGridView) R.drawable.baseline_grid_view_24
                                else R.drawable.baseline_view_list_24
                            ),
                            contentDescription = stringResource(
                                if (isGridView) R.string.grid else R.string.list
                            ),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { navController.navigate(Screen.AboutScreen.route) }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_info_24),
                            contentDescription = stringResource(R.string.about),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.FormScreen.route) }) {
                Icon(
                    painter = painterResource(R.drawable.baseline_add_box_24),
                    contentDescription = stringResource(R.string.tambah_tugas)
                )
            }
        }
    ) { padding ->
        if (tugasList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.tugas_kosong))
            }
        } else {
            if (isGridView) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .padding(8.dp)
                        .padding(padding)
                ) {
                    items(tugasList.size) { index ->
                        TugasItem(tugasList[index], onClick = { showDialog = it })
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(8.dp)
                        .padding(padding)
                ) {
                    items(tugasList.size) { index ->
                        TugasItem(tugasList[index], onClick = { showDialog = it })
                    }
                }
            }
        }
    }

    showDialog?.let { tugas ->
        DetailDialog(
            tugas = tugas,
            onDismiss = { showDialog = null },
            onDelete = {
                viewModel.hapusTugas(tugas)
                Toast.makeText(context, R.string.hapus, Toast.LENGTH_SHORT).show()
                showDialog = null
            },
            onEdit = {
                navController.navigate("${Screen.FormScreen.route}?id=${tugas.id}")
                showDialog = null
            }
        )
    }
}

@Composable
fun TugasItem(tugas: Tugas, onClick: (Tugas) -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable { onClick(tugas) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = tugas.nama_tugas, style = MaterialTheme.typography.titleMedium)
            Text(text = tugas.prioritas, style = MaterialTheme.typography.bodySmall)
            Text(text = tugas.deadline, style = MaterialTheme.typography.labelSmall)
        }
    }
}