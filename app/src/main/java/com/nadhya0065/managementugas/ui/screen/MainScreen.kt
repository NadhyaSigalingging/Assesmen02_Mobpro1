package com.nadhya0065.managementugas.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nadhya0065.managementugas.R
import com.nadhya0065.managementugas.model.Tugas
import com.nadhya0065.managementugas.navigation.Screen
import com.nadhya0065.managementugas.ui.theme.ManagemenTugasTheme
import com.nadhya0065.managementugas.util.SettingsDataStore
import com.nadhya0065.managementugas.util.ViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current
    val dataStore = remember { SettingsDataStore(context) }
    val layoutFlow by dataStore.layoutFlow.collectAsState(initial = true)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        scope.launch { dataStore.saveLayout(!layoutFlow) }
                    }) {
                        Icon(
                            painter = painterResource(
                                if (layoutFlow) R.drawable.baseline_grid_view_24
                                else R.drawable.baseline_view_list_24
                            ),
                            contentDescription = stringResource(
                                if (layoutFlow) R.string.grid else R.string.list
                            ),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(onClick = {
                        navController.navigate(Screen.About.route)
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_info_24),
                            contentDescription = stringResource(R.string.tentang_aplikasi),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.FormBaru.route)
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.tambah_tugas),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        ScreenContent(
            showList = layoutFlow,
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
fun ScreenContent(
    showList: Boolean,
    modifier: Modifier = Modifier,
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel(factory = ViewModelFactory(context))
    val detailViewModel: DetailViewModel = viewModel(factory = ViewModelFactory(context))
    val data by viewModel.data.collectAsState()
    val scope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }
    val selectedTugas = remember { mutableStateOf<Tugas?>(null) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(stringResource(R.string.konfirmasi_hapus)) },
            text = { Text(stringResource(R.string.yakin_hapus)) },
            confirmButton = {
                TextButton(onClick = {
                    selectedTugas.value?.let {
                        detailViewModel.delete(it)
                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = context.getString(R.string.data_dihapus),
                                actionLabel = context.getString(R.string.undo)
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                detailViewModel.restoreDeletedTugas()
                            }
                        }
                    }
                    openDialog.value = false
                }) {
                    Text(stringResource(android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    openDialog.value = false
                }) {
                    Text(stringResource(android.R.string.cancel))
                }
            }
        )
    }

    if (data.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.list_kosong))
        }
    } else {
        if (showList) {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 84.dp)
            ) {
                items(data) { tugas ->
                    ListItem(
                        tugas = tugas,
                        onClick = { navController.navigate(Screen.FormUbah.withId(tugas.id)) },
                        onDelete = {
                            selectedTugas.value = it
                            openDialog.value = true
                        }
                    )
                    HorizontalDivider()
                }
            }
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = modifier.fillMaxSize(),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 84.dp)
            ) {
                items(data) { tugas ->
                    GridItem(
                        tugas = tugas,
                        onClick = { navController.navigate(Screen.FormUbah.withId(tugas.id)) },
                        onDelete = {
                            selectedTugas.value = it
                            openDialog.value = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ListItem(tugas: Tugas, onClick: () -> Unit, onDelete: (Tugas) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = tugas.nama_tugas, fontWeight = FontWeight.Bold)
            Text(text = tugas.dekripsi)
            Text(text = tugas.prioritas)
        }
        IconButton(onClick = { onDelete(tugas) }) {
            Icon(Icons.Default.Delete, contentDescription = "Hapus")
        }
    }
}

@Composable
fun GridItem(tugas: Tugas, onClick: () -> Unit, onDelete: (Tugas) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, DividerDefaults.color)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = tugas.nama_tugas, fontWeight = FontWeight.Bold)
            Text(text = tugas.dekripsi)
            Text(text = tugas.prioritas)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { onDelete(tugas) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Hapus")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    ManagemenTugasTheme {
        MainScreen(rememberNavController())
    }
}
