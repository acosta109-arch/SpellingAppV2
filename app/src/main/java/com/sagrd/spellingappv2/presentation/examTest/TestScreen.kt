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
import androidx.compose.ui.tooling.preview.Preview
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
        onPlayDescripcion = viewModel::playDescripcion,
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
    onPlayDescripcion: (String) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit
) {
    val isDarkMode = isSystemInDarkTheme()
    var showStartDialog by remember { mutableStateOf(true) }
    var showExitDialog by remember { mutableStateOf(false) }
    var showCompletionDialog by remember { mutableStateOf(false) }

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
        Color.White
    else
        Color.White

    val borderColor = if (isDarkMode) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.5f)

    if (showCompletionDialog || (uiState.palabras.isNotEmpty() && uiState.palabraActual == uiState.totalPalabras - 1)) {
        AlertDialog(
            onDismissRequest = { showCompletionDialog = false },
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
                        "Test Completado",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            },
            text = {
                Text(
                    "Has completado el test para tu hijo. ¡Felicidades!",
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
                    Text("Salir del Test")
                }
            },
            containerColor = Color.White,
            titleContentColor = textColor,
            textContentColor = textColor
        )
    }

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
            containerColor = Color.White,
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

                        Button(
                            onClick = {
                                if (uiState.palabraActual == uiState.totalPalabras - 1) {
                                    showCompletionDialog = true
                                } else {
                                    onNext()
                                }
                            },
                            enabled = uiState.palabraActual < uiState.totalPalabras - 1,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primaryButtonColor,
                                contentColor = Color.White,
                                disabledContainerColor = primaryButtonColor.copy(alpha = 0.5f),
                                disabledContentColor = Color.White.copy(alpha = 0.7f)
                            )
                        ) {
                            Text(if (uiState.palabraActual == uiState.totalPalabras - 1) "Terminar" else "Siguiente")
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

@Composable
fun TestContent(
    palabra: PalabraEntity,
    cardColor: Color,
    textColor: Color,
    borderColor: Color,
    onPlayAudio: () -> Unit,
    onPlayDescripcion: () -> Unit
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
                Image(
                    painter = painterResource(id = R.drawable.palabras),
                    contentDescription = "Imagen predeterminada",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = palabra.descripcion,
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onPlayDescripcion,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bocina),
                        contentDescription = "Reproducir descripción",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun preview() {
    TestBody(
        uiState = TestUiState(),
        onBack = {},
        onPlayAudio = {},
        onPlayDescripcion = {},
        onNext = {},
        onPrevious = {}
    )

}