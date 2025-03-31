package com.sagrd.spellingappv2.presentation.logros

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sagrd.spellingappv2.data.remote.dto.LogrosDto
import edu.ucne.spellingapp.R

@Composable
fun ProgresoScreen(
    viewModel: LogrosViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onMenuClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LogrosBodyListScreen(
        uiState = uiState,
        onBack = onBack,
        onMenuClick = onMenuClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LogrosBodyListScreen(
    uiState: LogrosUiState,
    onBack: () -> Unit,
    onMenuClick: () -> Unit
) {
    val isDarkMode = isSystemInDarkTheme()
    var searchText by remember { mutableStateOf("") }
    var showPodium by remember { mutableStateOf(false) }

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
    val cardColor = if (isDarkMode) Color(0xFF1F2937).copy(alpha = 0.7f) else Color.White.copy(alpha = 0.7f)
    val searchCardColor = if (isDarkMode) Color(0xFF1F2937) else Color.White
    val borderColor = if (isDarkMode) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.5f)

    val filteredLogros = if (searchText.isBlank()) {
        uiState.logros
    } else {
        uiState.logros.filter {
            it.nombreCompleto.contains(searchText, ignoreCase = true)
        }
    }

    // Calcular el podio (los 3 nombres más repetidos)
    val podiumData = uiState.logros
        .groupBy { it.nombreCompleto }
        .map { (name, logros) -> Pair(name, logros.size) }
        .sortedByDescending { it.second }
        .take(3)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Progreso de hijos", fontWeight = FontWeight.Bold, color = Color.White) },
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
                    .padding(16.dp)
            ) {
                // Filtro por nombre y botón de podio
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = searchCardColor
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = if (isDarkMode) Color.White else Color.Gray,
                            modifier = Modifier.padding(end = 8.dp)
                        )

                        TextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            placeholder = { Text("Filtrar por nombre") },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.Transparent)
                        )

                        if (searchText.isNotEmpty()) {
                            IconButton(
                                onClick = { searchText = "" }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Limpiar búsqueda",
                                    tint = if (isDarkMode) Color.White else Color.Gray
                                )
                            }
                        }

                        // Botón para mostrar/ocultar el podio usando la imagen de star
                        IconButton(
                            onClick = { showPodium = !showPodium },
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .background(
                                    color = if (showPodium) {
                                        if (isDarkMode) Color(0xFF177882) else Color(0xFF76D7EA)
                                    } else {
                                        Color.Transparent
                                    },
                                    shape = CircleShape
                                )
                                .size(36.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.star),
                                contentDescription = "Mostrar podio",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                // Mostrar el podio si está activo
                if (showPodium) {
                    PodiumView(
                        podiumData = podiumData,
                        isDarkMode = isDarkMode,
                        cardColor = cardColor
                    )
                }

                // Lista de logros filtrada
                if (!showPodium) {
                    if (filteredLogros.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No se encontraron resultados",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (isDarkMode) Color.White else Color.Black
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(filteredLogros) { logro ->
                                LogroRow(
                                    logro = logro,
                                    cardColor = cardColor,
                                    borderColor = borderColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PodiumView(
    podiumData: List<Pair<String, Int>>,
    isDarkMode: Boolean,
    cardColor: Color
) {
    val goldColor = Color(0xFFFFD700)
    val silverColor = Color(0xFFC0C0C0)
    val bronzeColor = Color(0xFFCD7F32)

    // Lista de colores para cada posición del podio
    val medalColors = listOf(goldColor, silverColor, bronzeColor)
    val positions = listOf("1er Lugar", "2do Lugar", "3er Lugar")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = "RANKING DE ESTRELLAS",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = if (isDarkMode) Color.White else Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        if (podiumData.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay datos suficientes para mostrar el podio",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isDarkMode) Color.White else Color.Black
                    )
                }
            }
        } else {
            podiumData.forEachIndexed { index, (name, count) ->
                if (index < 3) {  // Solo mostrar los primeros 3
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = cardColor
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Medalla con color según posición
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(medalColors[index], CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.trofeo),
                                    contentDescription = "Medalla ${index + 1}",
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkMode) Color.White else Color.Black
                                )
                                Text(
                                    text = "$count logros",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isDarkMode) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.7f)
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = positions[index],
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = medalColors[index]
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LogroRow(
    logro: LogrosDto,
    cardColor: Color,
    borderColor: Color
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable { expanded = true }
            .border(width = 1.dp, color = borderColor, shape = MaterialTheme.shapes.medium),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "Imagen del Logro",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 12.dp),
                )
                Column {
                    Text(
                        text = logro.nombreCompleto,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black // Adjust color based on your theme
                    )
                    Text(
                        text = logro.mensaje,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black.copy(alpha = 0.8f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}