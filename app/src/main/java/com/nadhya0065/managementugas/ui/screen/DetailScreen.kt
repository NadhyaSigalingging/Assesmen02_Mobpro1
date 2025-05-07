package com.nadhya0065.managementugas.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.nadhya0065.managementugas.R
import com.nadhya0065.managementugas.ui.theme.ManagemenTugasTheme
import com.nadhya0065.managementugas.util.ViewModelFactory
import kotlinx.coroutines.launch

const val KEY_VAL_TUGAS = "idTugas"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var namaTugas by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val radioOptions = listOf(
        stringResource(id = R.string.sudahdekat_deadline),
        stringResource(id = R.string.deadline_masihlama),
    )
    var prioritas by remember { mutableStateOf(radioOptions[0]) }

    LaunchedEffect(Unit) {
        if (id != null) {
            val data = viewModel.getTugas(id)
            data?.let {
                namaTugas = it.nama_tugas
                deskripsi = it.dekripsi
                prioritas = it.prioritas
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    Text(
                        text = if (id == null) stringResource(id = R.string.tambah_tugas)
                        else stringResource(id = R.string.edit_tugas)
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        if (namaTugas.isEmpty() || deskripsi.isEmpty() || prioritas.isEmpty()) {
                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                            return@IconButton
                        }
                        if (id == null) {
                            viewModel.insert(namaTugas, deskripsi, prioritas)
                        } else {
                            viewModel.update(id, namaTugas, deskripsi, prioritas)
                        }
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null) {
                        DeleteAction { showDialog = true }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        FormMahasiswa(
            tugas = namaTugas,
            onTugasChange = { namaTugas = it },
            deskripsi = deskripsi,
            onDeskripsiChange = { deskripsi = it },
            prioritas = prioritas,
            onPrioritasChange = { prioritas = it },
            radioOptions = radioOptions,
            modifier = Modifier.padding(padding)
        )
        if (id != null && showDialog) {
            DisplayAlertDialog(
                onDismissRequest = { showDialog = false },
                onConfirmation = {
                    showDialog = false
                    coroutineScope.launch {
                        val deletedTugas = viewModel.getTugas(id)
                        deletedTugas?.let {
                            viewModel.delete(it)
                            val result = snackbarHostState.showSnackbar(
                                message = context.getString(R.string.data_dihapus),
                                actionLabel = context.getString(R.string.undo)
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                viewModel.restoreDeletedTugas()
                            }
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.hapus)) },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}

@Composable
fun ClassOption(
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun FormMahasiswa(
    tugas: String, onTugasChange: (String) -> Unit,
    deskripsi: String, onDeskripsiChange: (String) -> Unit,
    prioritas: String, onPrioritasChange: (String) -> Unit,
    radioOptions: List<String>,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = tugas,
            onValueChange = { onTugasChange(it) },
            label = { Text(text = stringResource(R.string.tugas)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = deskripsi,
            onValueChange = { onDeskripsiChange(it) },
            label = { Text(text = stringResource(R.string.deskripsi)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(id = R.string.prioritas),
            style = MaterialTheme.typography.labelLarge
        )
        Column(
            modifier = Modifier.border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            radioOptions.forEach { text ->
                ClassOption(
                    label = text,
                    isSelected = prioritas == text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    onClick = { onPrioritasChange(text) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    ManagemenTugasTheme {
        DetailScreen(navController = rememberNavController())
    }
}
