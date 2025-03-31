package com.sagrd.spellingappv2.presentation.estadisticas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.spellingapp.R

@Composable
fun EstadisticaListScreen(
    viewModel: EstadisticasViewModel = hiltViewModel(),
    onMenuClick: () -> Unit,
    goBack: () -> Unit,
    onNavigateToProgreso: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StatisticsBodyScreen(
        uiState = uiState,
        onMenuClick = onMenuClick,
        goBack = goBack,
        onNavigateToProgreso = onNavigateToProgreso
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsBodyScreen(
    uiState: EstadisticaUiState,
    onMenuClick: () -> Unit,
    goBack: () -> Unit,
    onNavigateToProgreso: () -> Unit
) {
    val isDarkMode = isSystemInDarkTheme()

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

    val textGradient = Brush.linearGradient(
        colors = gradientColors
    )

    val appBarColor = if (isDarkMode) Color(0xFF283653) else Color(0xFF7FB3D5)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Estadísticas",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
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
                },
                actions = {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
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
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Resumen de Datos",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Hijos Card
                    StatCard(
                        title = "Hijos",
                        count = uiState.hijosCount,
                        gradient = textGradient,
                        imageResId = R.drawable.hijos,
                        modifier = Modifier.weight(1f)
                    )

                    // Pines Card
                    StatCard(
                        title = "Pines",
                        count = uiState.pinesCount,
                        gradient = textGradient,
                        imageResId = R.drawable.codigos,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Palabras Card
                    StatCard(
                        title = "Palabras",
                        count = uiState.palabrasCount,
                        gradient = textGradient,
                        imageResId = R.drawable.palabras,
                        modifier = Modifier.weight(1f)
                    )

                    // Estrellas Card
                    StatCard(
                        title = "Estrellas",
                        count = uiState.logrosCount,
                        gradient = textGradient,
                        imageResId = R.drawable.star,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Additional statistics or charts could go here

                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                }

                uiState.errorMessage?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Button(
                    onClick = onNavigateToProgreso,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF177882)
                    )
                ) {
                    Text(
                        text = "Ver Progreso",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    count: Int,
    gradient: Brush,
    imageResId: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(150.dp)
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Image above the title
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .padding(bottom = 4.dp)
            )

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Gradient text effect for the count
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = count.toString(),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        brush = gradient
                    )
                )
            }
        }
    }
}