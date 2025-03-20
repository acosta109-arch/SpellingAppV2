package com.sagrd.spellingappv2.presentation.hijos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HijosScreen(
    viewModel: hijosViewModel = hiltViewModel(),
    goBack: () -> Unit,
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HijoBodyScreen(
        uiState = uiState,
        goBack = goBack,
        onSave = viewModel::saveHijo,
        onNombreChange = viewModel::onNombreChange,
        onApellidoChange = viewModel::onApellidoChange,
        onGeneroChange = viewModel::onGeneroChange,
        onEdadChange = viewModel::onEdadChange,
        onUsuarioIdChange = viewModel::onUsuarioIdChange,
        onPinChange = viewModel::onPinIdChange
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HijoBodyScreen(
    uiState: Uistate,
    goBack: () -> Unit,
    onPinChange: (String) -> Unit,
    onSave: () -> Unit,
    onNombreChange: (String) -> Unit,
    onApellidoChange: (String) -> Unit,
    onGeneroChange: (String) -> Unit,
    onEdadChange: (Int) -> Unit,
    onUsuarioIdChange: (Int) -> Unit,
){

    var expandedPin by remember { mutableStateOf(false) }
    var expandedGenero by remember { mutableStateOf(false) }

    Scaffold(

    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ){
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = onNombreChange,
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = uiState.apellido,
                onValueChange = onApellidoChange,
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown para selección de género
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedGenero = true },
                    label = { Text("Género") },
                    value = uiState.genero,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
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
                                onGeneroChange(genero)
                                expandedGenero = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = uiState.edad.toString(),
                onValueChange = { onEdadChange(it.toIntOrNull() ?: 0) },
                label = { Text("Edad") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedPin = true },
                    label = { Text("Pin") },
                    value = uiState.pines.firstOrNull { it.pinId.toString() == uiState.pinId }?.pin
                        ?: "",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
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
                                onPinChange(tecnico.pinId.toString()) ?: println("Pin no encontrado")
                                expandedPin = false
                            }
                        )
                    }
                }
            }
            uiState.errorMessage?.let {
                Text(text = it, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(16.dp).weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.padding(15.dp),
                    onClick = {
                        goBack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(red = 0, green = 200, blue = 210, alpha = 255),
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Volver")
                }
                Button(
                    modifier = Modifier.padding(15.dp),
                    onClick = {
                        onSave()
                        goBack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(red = 0, green = 200, blue = 210, alpha = 255),
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Crear"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Crear")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HijoScreenPreview(){
    HijoBodyScreen(
        uiState = Uistate(
            nombre = "Juan",
            apellido = "Perez",
            edad = 12,
            genero = "Masculino",
            pinId = "1234",
            usuarioId = 1,
        ),
        goBack = {},
        onSave = {},
        onNombreChange = {},
        onApellidoChange = {},
        onGeneroChange = {},
        onEdadChange = {},
        onUsuarioIdChange = {},
        onPinChange = {}
    )
}