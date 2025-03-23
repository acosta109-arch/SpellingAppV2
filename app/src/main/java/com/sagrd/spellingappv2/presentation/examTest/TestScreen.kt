package com.sagrd.spellingappv2.presentation.examTest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sagrd.spellingappv2.data.local.entities.PalabraEntity
import edu.ucne.spellingapp.R

@Composable
fun TestScreen(
    viewModel: TestViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TestBody(
        uiState = uiState,
        onBack = onBack,
        onPlayAudio = viewModel::playAudio,
        onNext = viewModel::nextPalabra,
        onPrevious = viewModel::previousPalabra
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TestBody(
    uiState: TestUiState,
    onBack: () -> Unit,
    onPlayAudio: (String) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit
) {
    val isDarkMode = isSystemInDarkTheme()
    var showStartDialog by remember { mutableStateOf(true) }
    val context = LocalContext.current

    // Definir los colores de gradiente basados en el modo oscuro o claro
    val gradientColors = if (isDarkMode) {
        listOf(
            Color(0xFF283653),
            Color(0xFF003D42),
            Color(0xFF177882)
        )
    } else {
        listOf(
            Color(0xFF7FB3D5),
            Color(0xFF76D7EA),
            Color(0xFFAED6F1)
        )
    }

    // Color del AppBar basado en el modo
    val appBarColor = if (isDarkMode) Color(0xFF283653) else Color(0xFF7FB3D5)

    // Color del texto adaptado al tema
    val textColor = if (isDarkMode) Color.White else Color.Black

    // Color de las cards adaptado al tema
    val cardColor = if (isDarkMode)
        Color(0xFF1F2937).copy(alpha = 0.7f)
    else
        Color.White.copy(alpha = 0.7f)

    // Color del borde de las cards
    val borderColor = if (isDarkMode) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.5f)

    // Diálogo de inicio del test
    if (showStartDialog) {
        AlertDialog(
            onDismissRequest = { showStartDialog = false },
            title = { Text("Iniciar Test") },
            text = { Text("¿Quieres comenzar el test de palabras?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showStartDialog = false
                    }
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onBack
                ) {
                    Text("No")
                }
            },
            containerColor = cardColor,
            titleContentColor = textColor,
            textContentColor = textColor
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Test de Palabras", fontWeight = FontWeight.Bold, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = appBarColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
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
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = gradientColors
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                if (uiState.palabras.isNotEmpty() && !showStartDialog) {
                    val currentPalabra = uiState.palabras[uiState.palabraActual]

                    // Contenido principal del test
                    TestContent(
                        palabra = currentPalabra,
                        cardColor = cardColor,
                        textColor = textColor,
                        borderColor = borderColor,
                        onPlayAudio = { onPlayAudio(currentPalabra.nombre) }
                    )

                    // Botones de navegación
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = onPrevious,
                            enabled = uiState.palabraActual > 0
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Anterior"
                            )
                            Text("Anterior")
                        }

                        Button(
                            onClick = onNext,
                            enabled = uiState.palabraActual < uiState.totalPalabras - 1
                        ) {
                            Text("Siguiente")
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Siguiente"
                            )
                        }
                    }

                    // Barra de progreso
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LinearProgressIndicator(
                            progress = uiState.porcentajeCompletado,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )

                        Text(
                            text = "Progreso: ${((uiState.porcentajeCompletado * 100).toInt())}%",
                            style = MaterialTheme.typography.bodyMedium,
                            color = textColor,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                } else if (uiState.isLoading) {
                    // Mensaje de carga
                    Text(
                        text = "Cargando palabras...",
                        color = textColor
                        // No modifier.align needed here since Column already has horizontalAlignment = Alignment.CenterHorizontally
                    )
                } else if (uiState.errorMessage != null) {
                    // Mensaje de error
                    Text(
                        text = "Error: ${uiState.errorMessage}",
                        color = Color.Red
                        // No modifier.align needed here
                    )
                }
            }
        }
    }
}

@Composable
fun TestContent(
    palabra: PalabraEntity,
    cardColor: Color,
    textColor: Color,
    borderColor: Color,
    onPlayAudio: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .border(width = 1.dp, color = borderColor, shape = MaterialTheme.shapes.medium),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen de la palabra
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
                        .height(200.dp)
                        .border(1.dp, borderColor, MaterialTheme.shapes.small)
                )
            } ?: run {
                // Imagen por defecto si no hay URL
                Image(
                    painter = painterResource(id = R.drawable.palabras),
                    contentDescription = "Imagen predeterminada",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre de la palabra con icono de audio
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = palabra.nombre,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
                IconButton(onClick = onPlayAudio) {
                    Image(
                        painter = painterResource(id = R.drawable.bocina),
                        contentDescription = "Reproducir audio",
                        colorFilter = ColorFilter.tint(textColor),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción de la palabra
            Text(
                text = palabra.descripcion,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor,
                textAlign = TextAlign.Center
            )
        }
    }
}