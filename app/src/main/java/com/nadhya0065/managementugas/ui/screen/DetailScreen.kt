package com.nadhya0065.managementugas.ui.screen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
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
import java.util.*

const val KEY_VAL_TUGAS = "idTugas"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var namaTugas by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val radioOptions = listOf(
        stringResource(id = R.string.sudahdekat_deadline),
        stringResource(id = R.string.deadline_masihlama),
    )
    var prioritas by remember { mutableStateOf(radioOptions[0]) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (id != null) {
            val data = viewModel.getTugas(id)
            data?.let {
                namaTugas = it.nama_tugas
                deskripsi = it.dekripsi
                deadline = it.prioritas
                prioritas = it.deadline
            }
        }
    }


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
                        if (namaTugas.isBlank() || deskripsi.isBlank() || prioritas.isEmpty() || deadline.isBlank()) {
                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                        } else {
                            if (id == null) {
                                viewModel.insert(namaTugas, deskripsi, prioritas, deadline)
                            } else {
                                viewModel.update(id, namaTugas, deskripsi, prioritas, deadline)
                            }
                            navController.popBackStack()
                        }
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
        }
    ) { padding ->
        FormTugas(
            tugas = namaTugas,
            onTugasChange = { namaTugas = it },
            deskripsi = deskripsi,
            onDeskripsiChange = { deskripsi = it },
            prioritas = prioritas,
            onPrioritasChange = { prioritas = it },
            radioOptions = radioOptions,
            modifier = Modifier.padding(padding),
            deadline = deadline,
            onDeadlineChange = { deadline = it },
        )

        if (id != null && showDialog) {
            DisplayAlertDialog(
                onDismissRequest = { showDialog = false },
                onConfirmation = {
                    coroutineScope.launch {
                        viewModel.delete(id)
                        navController.popBackStack()
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
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.hapus)) },
            onClick = {
                expanded = false
                delete()
            }
        )
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
        modifier = modifier.padding(8.dp),
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

@SuppressLint("DefaultLocale")
@Composable
fun FormTugas(
    tugas: String, onTugasChange: (String) -> Unit,
    deskripsi: String, onDeskripsiChange: (String) -> Unit,
    deadline: String, onDeadlineChange: (String) -> Unit,
    prioritas: String, onPrioritasChange: (String) -> Unit,
    radioOptions: List<String>,
    modifier: Modifier
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = tugas,
            onValueChange = onTugasChange,
            label = { Text(stringResource(R.string.tugas)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = deskripsi,
            onValueChange = onDeskripsiChange,
            label = { Text(stringResource(R.string.deskripsi)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = deadline,
            onValueChange = onDeadlineChange,
            label = { Text(stringResource(R.string.deadline)) },
            trailingIcon = {
                IconButton(onClick = {
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            val formattedDate =
                                String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year)
                            onDeadlineChange(formattedDate)
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Pilih Tanggal"
                    )
                }
            },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.prioritas),
            style = MaterialTheme.typography.labelLarge
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            radioOptions.forEach { text ->
                ClassOption(
                    label = text,
                    isSelected = prioritas == text,
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
