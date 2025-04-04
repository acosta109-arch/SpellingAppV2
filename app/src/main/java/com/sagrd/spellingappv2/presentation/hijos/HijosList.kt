package com.sagrd.spellingappv2.presentation.hijos

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sagrd.spellingappv2.data.local.entities.HijoEntity
import com.sagrd.spellingappv2.data.local.entities.PinEntity
import edu.ucne.spellingapp.R

@Composable
fun HijosListScreen(
    viewModel: HijosViewModel = hiltViewModel(),
    onCreate: () -> Unit,
    onDelete: (Int) -> Unit,
    onEdit: (Int) -> Unit,
    onMenuClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HijosBodyList(
        uiState = uiState,
        onCreate = onCreate,
        onDelete = onDelete,
        onEdit = onEdit,
        onMenuClick = onMenuClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HijosBodyList(
    uiState: HijoUistate,
    onCreate: () -> Unit,
    onDelete: (Int) -> Unit,
    onEdit: (Int) -> Unit,
    onMenuClick: () -> Unit,
) {
    val context = LocalContext.current
    val isDarkMode = isSystemInDarkTheme()

    val shareHijoInfo = { nombre: String, pin: String ->
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Hola, $nombre! 📚✨  \n" +
                        "\n" +
                        "Hoy es un gran día para aprender inglés con *SpellingApp*. 🎉 ¡Inicia sesión ahora y diviértete mejorando tu ortografía! 🚀  \n" +
                        "\n" +
                        "‼️Tu código para iniciar sesión‼️:️️ \n" +
                        "\n" +
                        "*$pin*"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, null)
        context.startActivity(shareIntent)
    }

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hijos", fontWeight = FontWeight.Bold, color = Color.White) },
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
        },
        floatingActionButton = {
            val fabGradient = Color(0xFF5DADE2)

            FloatingActionButton(
                onClick = onCreate,
                containerColor = fabGradient
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Agregar Hijo",
                    tint = if (isDarkMode) Color.White else Color.White
                )
            }
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
                if(uiState.hijos.isEmpty()){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.nodates),
                            contentDescription = "No dates",
                        )
                        Text(text = "No se encontraron Hijos", color = textColor)
                    }
                }else{
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.hijos) { hijo ->
                            HijosRow(
                                hijos = hijo,
                                onDelete = onDelete,
                                onEdit = onEdit,
                                pines = uiState.pines,
                                cardColor = cardColor,
                                textColor = textColor,
                                borderColor = borderColor,
                                onShare = shareHijoInfo
                            )
                        }
                    }
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
    pines: List<PinEntity>,
    cardColor: Color,
    textColor: Color,
    borderColor: Color,
    onShare: (String, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val pinHijo = pines.find { pin ->
        pin.pinId == hijos.pinId.toIntOrNull()
    }?.pin ?: "Pin no encontrado"

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
                        painter = painterResource(id = if (hijos.genero == "Masculino") R.drawable.hijo else R.drawable.hija),
                        contentDescription = "Imagen del Hijo",
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 5.dp),
                    )
                    Text(
                        text = hijos.nombre + " " + hijos.apellido,
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
                        DropdownMenuItem(
                            text = { Text("Compartir") },
                            onClick = {
                                onShare(hijos.nombre, pinHijo)
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
                    color = textColor
                )
                Text(
                    text = "Edad: " + hijos.edad.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor
                )
                Text(
                    text = "Pin: $pinHijo",
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor
                )
            }
        }
    }
}

