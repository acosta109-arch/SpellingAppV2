package com.sagrd.spellingappv2.presentation.pin

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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PinDelete(
    viewModel: PinViewModel = hiltViewModel(),
    goBack: () -> Unit,
    pinId: Int,
    onMenuClick: () -> Unit
) {
    LaunchedEffect(pinId) {
        viewModel.selectedPines(pinId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PinBodyDelete(
        uiState = uiState,
        goBack = goBack,
        onDelete = viewModel::deletePin,
        onMenuClick = onMenuClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinBodyDelete(
    uiState: Uistate,
    goBack: () -> Unit,
    onDelete: () -> Unit,
    onMenuClick: () -> Unit
) {
    val isDarkMode = isSystemInDarkTheme()

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

    // Colores para elementos de UI
    val textColor = if (isDarkMode) Color.White else Color.Black
    val borderColor = if (isDarkMode) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.5f)
    val accentColor = Color(0xFF5DADE2)
    val cardBgColor = if (isDarkMode) Color(0xFF1E2B3C) else Color.White

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Eliminar Pin", fontWeight = FontWeight.Bold, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = appBarColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
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
                    .padding(24.dp)
            ) {
                Text(
                    text = "¿Seguro que deseas eliminar?",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Center,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .border(
                            2.dp,
                            color = borderColor,
                            MaterialTheme.shapes.medium
                        )
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = cardBgColor
                    )
                ) {
                    Text(
                        text = "Pin: ${uiState.pin}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center,
                        color = textColor
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Botón Cancelar
                    Button(
                        modifier = Modifier.width(150.dp),
                        onClick = goBack,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor,
                            contentColor = Color.White
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Cancelar",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Cancelar",
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Botón Eliminar
                    Button(
                        modifier = Modifier.width(150.dp),
                        onClick = {
                            onDelete()
                            goBack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE74C3C), // Rojo para eliminar
                            contentColor = Color.White
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Eliminar",
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PinDeletePreview() {
    val fake = Uistate(
        pinId = 1, pin = "1234"
    )
    PinBodyDelete(
        uiState = fake,
        goBack = {},
        onDelete = {},
        onMenuClick = {}
    )
}