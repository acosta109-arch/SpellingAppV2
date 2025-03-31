package com.sagrd.spellingappv2.presentation.palabras

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
        onBack = onBack,
        onFilterChange = viewModel::filterPalabras
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PalabrasBodyList(
    uiState: PalabrasUiState,
    onBack: () -> Unit,
    onFilterChange: (String) -> Unit
) {
    val isDarkMode = isSystemInDarkTheme()
    var filterText by remember { mutableStateOf("") }

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

    val textColor = if (isDarkMode) Color.White else Color.Black

    val cardColor = if (isDarkMode)
        Color(0xFF1F2937).copy(alpha = 0.7f)
    else
        Color.White.copy(alpha = 0.7f)

    val borderColor = if (isDarkMode) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.5f)

    val filterBackgroundColor = if (isDarkMode)
        Color(0xFF1F2937).copy(alpha = 0.8f)
    else
        Color.White.copy(alpha = 0.8f)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Palabras", fontWeight = FontWeight.Bold, color = Color.White) },
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
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = filterText,
                    onValueChange = {
                        filterText = it
                        onFilterChange(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            filterBackgroundColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    placeholder = { Text("Buscar palabras...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = if (isDarkMode) Color.White else Color.Gray
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedTextColor = textColor,
                        focusedTextColor = textColor,
                        cursorColor = textColor,
                        focusedBorderColor = if (isDarkMode) Color.White else Color(0xFF5DADE2),
                        unfocusedBorderColor = borderColor,
                        unfocusedPlaceholderColor = if (isDarkMode) Color.White.copy(alpha = 0.6f) else Color.Gray,
                        focusedPlaceholderColor = if (isDarkMode) Color.White.copy(alpha = 0.6f) else Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.isLoading) {
                    Text("Cargando palabras...", color = textColor)
                } else if (uiState.errorMessage != null) {
                    // Error message
                    Text("Error: ${uiState.errorMessage}", color = Color.Red)
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.palabras) { palabra ->
                            PalabraRow(
                                palabra = palabra,
                                cardColor = cardColor,
                                textColor = textColor,
                                borderColor = borderColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PalabraRow(
    palabra: PalabraEntity,
    cardColor: Color,
    textColor: Color,
    borderColor: Color
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

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
        Column {
            Row(
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier.weight(1f),
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
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }
                Box {
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Ver detalles") },
                            onClick = {
                                // Acción para ver detalles
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
                    text = palabra.descripcion,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = textColor
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
                            .border(1.dp, borderColor, MaterialTheme.shapes.small)
                    )
                }
            }
        }
    }
}