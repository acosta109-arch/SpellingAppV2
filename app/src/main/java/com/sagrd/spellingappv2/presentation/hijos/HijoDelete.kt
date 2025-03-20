package com.sagrd.spellingappv2.presentation.hijos

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sagrd.spellingappv2.data.local.entities.HijoEntity
import com.sagrd.spellingappv2.data.local.entities.PinEntity

@Composable
fun HijoDelete(
    viewModel: hijosViewModel = hiltViewModel(),
    goBack: () -> Unit,
    hijoId: Int,
) {
    LaunchedEffect(hijoId) {
        viewModel.selectedHijos(hijoId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HijoBodyDelete(
        uiState = uiState,
        goBack = goBack,
        onDelete = viewModel::deleteHijo,
        pines = uiState.pines
    )
}

@Composable
fun HijoBodyDelete(
    uiState: Uistate,
    goBack: () -> Unit,
    onDelete: () -> Unit,
    pines: List<PinEntity>
) {
    val pinHijo = pines.find { pin ->
        pin.pinId == uiState.pinId.toIntOrNull()
    }?.pin ?: "Pin no encontrado"

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "¿Seguro que deseas eliminar?",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )
            Card(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .border(
                        2.dp,
                        color = Color.Black,
                        MaterialTheme.shapes.medium
                    )
                    .fillMaxWidth()

            ) {
                Text(
                    text = "Nobre: ${uiState.nombre + " " + uiState.apellido}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .border(
                        2.dp,
                        color = Color.Black,
                        MaterialTheme.shapes.medium
                    )
                    .fillMaxWidth()

            ) {
                Text(
                    text = "Genero: ${uiState.genero}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .border(
                        2.dp,
                        color = Color.Black,
                        MaterialTheme.shapes.medium
                    )
                    .fillMaxWidth()

            ) {
                Text(
                    text = "Edad: ${uiState.edad}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .border(
                        2.dp,
                        color = Color.Black,
                        MaterialTheme.shapes.medium
                    )
                    .fillMaxWidth()

            ) {
                Text(
                    text = "Pin: $pinHijo",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = goBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver"
                    )
                    Text("Cancelar")
                }
                Button(
                    onClick = {
                        onDelete()
                        goBack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error, // Fondo rojo
                        contentColor = MaterialTheme.colorScheme.onError // Color del texto en contraste
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar"
                    )
                    Text("Eliminar")
                }
            }
        }
    }
}

@Preview
@Composable
private fun HijoDeletePreview() {
    val fake = Uistate(
        nombre = "Juan",
        apellido = "Perez",
        genero = "Masculino",
        edad = 8,
        pinId = "asd",
    )
    HijoBodyDelete(
        uiState = fake,
        goBack = {},
        onDelete = {},
        pines = emptyList()
    )
}