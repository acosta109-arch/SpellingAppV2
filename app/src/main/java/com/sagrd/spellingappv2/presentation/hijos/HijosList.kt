package com.sagrd.spellingappv2.presentation.hijos

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sagrd.spellingappv2.data.local.entities.HijoEntity
import com.sagrd.spellingappv2.data.local.entities.PinEntity
import edu.ucne.spellingapp.R
import kotlinx.coroutines.launch

@Composable
fun HijosListScreen(
    viewModel: hijosViewModel = hiltViewModel(),
    onCreate: () -> Unit,
    onDelete: (Int) -> Unit,
    onBack: () -> Unit,
    onEdit: (Int) -> Unit,
    onMenuClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HijosBodyList(
        uiState = uiState,
        onCreate = onCreate,
        onDelete = onDelete,
        onBack = onBack,
        onEdit = onEdit,

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HijosBodyList(
    uiState: Uistate,
    onCreate: () -> Unit,
    onDelete: (Int) -> Unit,
    onBack: () -> Unit,
    onEdit: (Int) -> Unit,
) {
    Scaffold(
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
        },
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
                items(uiState.hijos) { pin ->
                    HijosRow(
                        hijos = pin,
                        onDelete = onDelete,
                        onEdit = onEdit,
                        pines = uiState.pines
                    )
                }
            }
        }
    }

}

@Composable
fun HijosRow(
    hijos: HijoEntity,
    onDelete: (Int) -> Unit,
    onEdit: (Int) -> Unit,
    pines: List<PinEntity>
) {
    var expanded by remember { mutableStateOf(false) }

    val pinHijo = pines.find { pin ->
        pin.pinId == hijos.pinId.toIntOrNull()
    }?.pin ?: "Sin Tecnico"

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
        Column {
            Row(
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Row(
                    modifier = Modifier
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Image(
                        painter = painterResource(id = R.drawable.hijos),
                        contentDescription = "Imagen del Pin",
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 5.dp),

                        )
                    Text(
                        text = hijos.nombre + " " + hijos.apellido,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box {
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Editar") },
                            onClick = {
                                onEdit(hijos.hijoId!!)
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar") },
                            onClick = {
                                onDelete(hijos.hijoId!!)
                                expanded = false
                            }
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            ) {

                Text(
                    text = "Genero: " + hijos.genero,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = "Edad: " + hijos.edad.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = "Pin: $pinHijo",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Preview
@Composable
private fun hijosListPreview() {
    val list = listOf(
        HijoEntity(
            hijoId = 1,
            nombre = "Juan",
            apellido = "Perez",
            edad = 5,
            genero = "Masculino",
            pinId = "asd",
            usuarioId = 1
        )
    )
    HijosBodyList(
        uiState = Uistate(hijos = list),
        onCreate = {},
        onDelete = {},
        onBack = {},
        onEdit = {}
    )
}