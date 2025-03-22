package com.sagrd.spellingappv2.presentation.palabras

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sagrd.spellingappv2.data.local.entities.PalabraEntity
import edu.ucne.spellingapp.R

@Composable
fun PalabrasListScreen(
    viewModel: PalabrasViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PalabrasBodyList(
        uiState = uiState,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PalabrasBodyList(
    uiState: PalabrasUiState,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Palabras", fontWeight = FontWeight.Bold) },
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
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (uiState.isLoading) {
                Text("Cargando palabras...")
            } else if (uiState.errorMessage != null) {
                // Error message
                Text("Error: ${uiState.errorMessage}", color = Color.Red)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(uiState.palabras) { palabra ->
                        PalabraRow(
                            palabra = palabra
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PalabraRow(
    palabra: PalabraEntity
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable { expanded = true }
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
                        painter = painterResource(id = R.drawable.palabras),
                        contentDescription = "Icono de Palabra",
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 5.dp),
                    )
                    Text(
                        text = palabra.nombre,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            ) {
                Text(
                    text = palabra.descripcion,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                palabra.fotoUrl?.let { url ->
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(url)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Imagen de ${palabra.nombre}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .border(1.dp, Color.Gray, MaterialTheme.shapes.small)
                    )
                }
            }
        }
    }
}
