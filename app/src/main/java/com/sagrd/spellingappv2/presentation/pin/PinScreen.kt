package com.sagrd.spellingappv2.presentation.pin

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PinScreen(
    viewModel: PinViewModel = hiltViewModel(),
    goBack: () -> Unit,
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PinBodyScreen(
        uiState = uiState,
        goBack = goBack,
        onPinChange = viewModel::onPinChange,
        onSave = viewModel::savePin
    )

}
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PinBodyScreen(
    uiState: Uistate,
    goBack: () -> Unit,
    onPinChange: (String) -> Unit,
    onSave: () -> Unit,
){
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Agregar Pin", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(red = 0, green = 200, blue = 210, alpha = 255),
                    titleContentColor = Color.White
                )
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ){
            OutlinedTextField(
                value = uiState.pin,
                onValueChange = onPinChange,
                label = { Text("Pin") },
                modifier = Modifier.fillMaxWidth()
            )
            uiState.errorMessage?.let {
                Text(text = it, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(16.dp))
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
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(red = 0, green = 200, blue = 210, alpha = 255),
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Guardar"
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
private fun PinScreenPreview(){
    PinBodyScreen(
        uiState = Uistate(
            pin = "1234"
        ),
        goBack = {},
        onPinChange = {},
        onSave = {}
    )
}