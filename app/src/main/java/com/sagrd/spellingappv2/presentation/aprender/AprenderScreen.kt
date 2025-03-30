package com.sagrd.spellingappv2.presentation.aprender

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
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.sagrd.spellingappv2.presentation.examTest.TestContent
import com.sagrd.spellingappv2.presentation.examTest.TestViewModel
import edu.ucne.spellingapp.R

@Composable
fun AprenderScreen(
    viewModel: AprenderViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onNavigateToLogros: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AprenderBody(
        uiState = uiState,
        onBack = onBack,
        onPlayAudio = viewModel::playAudio,
        onPlayDescripcion = viewModel::playDescripcion,
        onNext = viewModel::nextPalabra,
        onPrevious = viewModel::previousPalabra,
        onNavigateToLogros = onNavigateToLogros
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AprenderBody(
    uiState: AprenderUiState,
    onBack: () -> Unit,
    onPlayAudio: (String) -> Unit,
    onPlayDescripcion: (String) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onNavigateToLogros: () -> Unit
) {
    val isDarkMode = isSystemInDarkTheme()
    var showStartDialog by remember { mutableStateOf(true) }
    var showExitDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Eliminar el efecto que navega automáticamente
    // Ya no necesitamos este LaunchedEffect que navegaba automáticamente
    // LaunchedEffect(isTestCompleted) { ... }

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

    val appBarColor = if (isDarkMode) Color(0xFF283653) else Color(0xFF7FB3D5)

    val primaryButtonColor = if (isDarkMode) Color(0xFF177882) else Color(0xFF5499C7)
    val progressColor = if (isDarkMode) Color(0xFF177882) else Color(0xFF5499C7)
    val progressTrackColor = if (isDarkMode) Color(0xFF283653).copy(alpha = 0.3f) else Color(0xFFAED6F1).copy(alpha = 0.5f)

    val textColor = if (isDarkMode) Color.White else Color.Black

    val cardColor = if (isDarkMode)
        Color(0xFF1F2937).copy(alpha = 0.7f)
    else
        Color.White.copy(alpha = 0.7f)

    val borderColor = if (isDarkMode) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.5f)

    if (showStartDialog) {
        AlertDialog(
            onDismissRequest = { showStartDialog = false },
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.abeja),
                        contentDescription = "Abeja",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(bottom = 16.dp)
                    )
                    Text(
                        "Iniciar Test",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            },
            text = {
                Text(
                    "¿Quieres comenzar el test de palabras?",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showStartDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = primaryButtonColor
                    )
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onBack,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = primaryButtonColor
                    )
                ) {
                    Text("No")
                }
            },
            containerColor = cardColor,
            titleContentColor = textColor,
            textContentColor = textColor
        )
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.abeja),
                        contentDescription = "Abeja",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(bottom = 16.dp)
                    )
                    Text(
                        "Salir del Test",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            },
            text = {
                Text(
                    "¿Estás seguro de terminar el test?",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = onBack,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = primaryButtonColor
                    )
                ) {
                    Text("Sí, salir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showExitDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = primaryButtonColor
                    )
                ) {
                    Text("No, continuar")
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
                    IconButton(
                        onClick = { showExitDialog = true }
                    ) {
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

                    TestContent(
                        palabra = currentPalabra,
                        cardColor = cardColor,
                        textColor = textColor,
                        borderColor = borderColor,
                        onPlayAudio = { onPlayAudio(currentPalabra.nombre) },
                        onPlayDescripcion = { onPlayDescripcion(currentPalabra.descripcion) }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = onPrevious,
                            enabled = uiState.palabraActual > 0,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primaryButtonColor,
                                contentColor = Color.White,
                                disabledContainerColor = primaryButtonColor.copy(alpha = 0.5f),
                                disabledContentColor = Color.White.copy(alpha = 0.7f)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Anterior"
                            )
                            Text("Anterior")
                        }

                        // Modificamos el comportamiento del botón para separar las acciones de Siguiente y Finalizar
                        Button(
                            onClick = {
                                if (uiState.palabraActual == uiState.totalPalabras - 1) {
                                    // SOLO navegamos a LogrosScreen cuando se presiona el botón en la última palabra
                                    onNavigateToLogros()
                                } else {
                                    // En otro caso, simplemente vamos a la siguiente palabra
                                    onNext()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primaryButtonColor,
                                contentColor = Color.White
                            )
                        ) {
                            Text(if (uiState.palabraActual == uiState.totalPalabras - 1) "Finalizar" else "Siguiente")
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Siguiente"
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LinearProgressIndicator(
                            progress = uiState.porcentajeCompletado,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = progressColor,
                            trackColor = progressTrackColor
                        )

                        Text(
                            text = "Progreso: ${((uiState.porcentajeCompletado * 100).toInt())}%",
                            style = MaterialTheme.typography.bodyMedium,
                            color = textColor,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                } else if (uiState.isLoading) {
                    Text(
                        text = "Cargando palabras...",
                        color = textColor
                    )
                } else if (uiState.errorMessage != null) {
                    Text(
                        text = "Error: ${uiState.errorMessage}",
                        color = Color.Red
                    )
                }
            }
        }
    }
}