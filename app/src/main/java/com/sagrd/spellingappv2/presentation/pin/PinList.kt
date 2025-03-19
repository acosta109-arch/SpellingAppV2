package edu.ucne.registrotecnicos.presentation.pin

import edu.ucne.spellingapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sagrd.spellingappv2.data.local.entities.PinEntity
import com.sagrd.spellingappv2.presentation.pin.PinViewModel
import com.sagrd.spellingappv2.presentation.pin.Uistate

@Composable
fun PinListScreen(
    viewModel: PinViewModel = hiltViewModel(),
    onCreate: () -> Unit,
    onDelete: (Int) -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PinBodyListScreen(
        uiState = uiState,
        onCreate = onCreate,
        onDelete = onDelete,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PinBodyListScreen(
    uiState: Uistate,
    onCreate: () -> Unit,
    onDelete: (Int) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(/*
        topBar = {
            TopAppBar(
                title = { Text("Lista de Pines", fontWeight = FontWeight.Bold) },
               colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(red = 0, green = 100, blue = 100, alpha = 255),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },

            )
        },*/
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreate
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Pin")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(uiState.pins) { pin ->
                    PinRow(
                        pin = pin,
                        onDelete = onDelete,
                    )
                }
            }
        }
    }
}

@Composable
fun PinRow(
    pin: PinEntity,
    onDelete: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable { expanded = true } // Abre el menú al tocar
            .border(width = 2.dp, color = Color.Black, shape = MaterialTheme.shapes.medium),
        colors = CardDefaults.cardColors(
            containerColor = Color(red = 190, green = 240, blue = 60, alpha = 255)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Image(
                    painter = painterResource(id = R.drawable.codigos),
                    contentDescription = "Imagen del Pin",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = 8.dp),

                    )
                Text(
                    text = "Pin: ",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = pin.pin,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Box {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Eliminar") },
                        onClick = {
                            expanded = false
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }

    // Diálogo de confirmación para eliminar
    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            pinId = pin.pinId!!,
            pinCode = pin.pin,
            onConfirm = {
                onDelete(pin.pinId!!)
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

@Composable
fun DeleteConfirmationDialog(
    pinId: Int,
    pinCode: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar Eliminación") },
        text = {
            Text(
                "¿Está seguro que desea eliminar el pin '$pinCode'?",
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Eliminar", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Preview
@Composable
private fun PinListScreenPreview() {
    val list = listOf(
        PinEntity(pinId = 1, pin = "1234"),
        PinEntity(pinId = 2, pin = "5678"),
        PinEntity(pinId = 3, pin = "9012")
    )
    PinBodyListScreen(
        uiState = Uistate(pins = list),
        onCreate = {},
        onDelete = {},
        onBack = {}
    )
}