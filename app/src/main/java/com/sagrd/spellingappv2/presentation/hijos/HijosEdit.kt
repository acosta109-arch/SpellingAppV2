package com.sagrd.spellingappv2.presentation.hijos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HijosEditScreen(
    viewModel: HijosViewModel = hiltViewModel(),
    goBack: () -> Unit,
    hijoId: Int,
    onMenuClick: () -> Unit,
) {
    LaunchedEffect(hijoId) {
        viewModel.selectedHijos(hijoId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var canNavigate by remember { mutableStateOf(false) }

    HijoBodyEdit(
        uiState = uiState,
        goBack = goBack,
        onMenuClick = onMenuClick,
        onEvent = viewModel::onEvent,
    )

    LaunchedEffect(canNavigate) {
        if (canNavigate) {
            goBack()
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HijoBodyEdit(
    uiState: HijoUistate,
    goBack: () -> Unit,
    onMenuClick: () -> Unit,
    onEvent: (HijosEvent) -> Unit,
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

    val appBarColor = if (isDarkMode) Color(0xFF283653) else Color(0xFF7FB3D5)
    val textColor = if (isDarkMode) Color.White else Color.Black
    val borderColor =
        if (isDarkMode) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.5f)
    val accentColor = Color(0xFF5DADE2)
    var expandedPin by remember { mutableStateOf(false) }
    var expandedGenero by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Hijo", fontWeight = FontWeight.Bold, color = Color.White) },
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
                    .padding(24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = uiState.nombre,
                    onValueChange = { onEvent(HijosEvent.OnNombreChange(it)) },
                    label = { Text("Nombre", color = textColor) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        cursorColor = textColor,
                        focusedBorderColor = if (isDarkMode) Color.White else accentColor,
                        unfocusedBorderColor = borderColor,
                        focusedLabelColor = if (isDarkMode) Color.White else accentColor,
                        unfocusedLabelColor = textColor
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = uiState.apellido,
                    onValueChange = { onEvent(HijosEvent.OnApellidoChange(it)) },
                    label = { Text("Apellido", color = textColor) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        cursorColor = textColor,
                        focusedBorderColor = if (isDarkMode) Color.White else accentColor,
                        unfocusedBorderColor = borderColor,
                        focusedLabelColor = if (isDarkMode) Color.White else accentColor,
                        unfocusedLabelColor = textColor
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedGenero = true },
                        label = { Text("Género", color = textColor) },
                        value = uiState.genero,
                        onValueChange = {},
                        readOnly = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor,
                            cursorColor = textColor,
                            focusedBorderColor = if (isDarkMode) Color.White else accentColor,
                            unfocusedBorderColor = borderColor,
                            focusedLabelColor = if (isDarkMode) Color.White else accentColor,
                            unfocusedLabelColor = textColor
                        ),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = textColor,
                                modifier = Modifier.clickable { expandedGenero = true }
                            )
                        }
                    )
                    DropdownMenu(
                        expanded = expandedGenero,
                        onDismissRequest = { expandedGenero = false }
                    ) {
                        listOf("Masculino", "Femenino").forEach { genero ->
                            DropdownMenuItem(
                                text = { Text(genero) },
                                onClick = {
                                    onEvent(HijosEvent.OnGeneroChange(genero))
                                    expandedGenero = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = uiState.edad.toString(),
                    onValueChange = { onEvent(HijosEvent.OnEdadChange(it)) },
                    label = { Text("Edad", color = textColor) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        cursorColor = textColor,
                        focusedBorderColor = if (isDarkMode) Color.White else accentColor,
                        unfocusedBorderColor = borderColor,
                        focusedLabelColor = if (isDarkMode) Color.White else accentColor,
                        unfocusedLabelColor = textColor
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedPin = true },
                        label = { Text("Pin", color = textColor) },
                        value = uiState.pines.firstOrNull { it.pinId.toString() == uiState.pinId }?.pin
                            ?: "",
                        onValueChange = {},
                        readOnly = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor,
                            cursorColor = textColor,
                            focusedBorderColor = if (isDarkMode) Color.White else accentColor,
                            unfocusedBorderColor = borderColor,
                            focusedLabelColor = if (isDarkMode) Color.White else accentColor,
                            unfocusedLabelColor = textColor
                        ),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = textColor,
                                modifier = Modifier.clickable { expandedPin = true }
                            )
                        }
                    )
                    DropdownMenu(
                        expanded = expandedPin,
                        onDismissRequest = { expandedPin = false }
                    ) {
                        uiState.pines.forEach { tecnico ->
                            DropdownMenuItem(
                                text = { Text(tecnico.pin) },
                                onClick = {
                                    onEvent(HijosEvent.OnPinChange(tecnico.pin))
                                    expandedPin = false
                                }
                            )
                        }
                    }
                }

                uiState.errorMessage?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                        .weight(1f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
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
                                contentDescription = "Volver",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Volver",
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Button(
                        modifier = Modifier.width(150.dp),
                        onClick = { onEvent(HijosEvent.OnSave) },
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
                                imageVector = Icons.Default.Add,
                                contentDescription = "Modificar",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Modificar",
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