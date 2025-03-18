package com.sagrd.spellingappv2.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.spellingapp.R

@Composable
fun LoginScreen(
    viewModel: UsuarioViewModel = hiltViewModel(),
    goBack: () -> Unit,
    goToDashboard: () -> Unit,
    goToRegistrar: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LoginBodyScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onContrasenaChange = viewModel::onContrasenaChange,
        login = { email, contrasena ->
            viewModel.login(email, contrasena)
            if (uiState.successMessage != null) {
                goToDashboard()
            }
        },
        goToRegistrar = goToRegistrar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginBodyScreen(
    uiState: UsuarioViewModel.UiState,
    onEmailChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    login: (String, String) -> Unit,
    goToRegistrar: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bienvenido/a",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF006465))
                .padding(12.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painter = painterResource(id = R.drawable.abeja),
            contentDescription = "Logo",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(50)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFBEEE3B))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Login",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .padding(8.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Correo Electrónico", color = Color.Black) },
                    value = email,
                    onValueChange = {
                        email = it
                        onEmailChange(it)
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black
                    ),
                    textStyle = TextStyle(color = Color.Black)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Contraseña") },
                    value = contrasena,
                    onValueChange = {
                        contrasena = it
                        onContrasenaChange(it)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black
                    ),
                    textStyle = TextStyle(color = Color.Black)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    modifier = Modifier.width(180.dp),
                    onClick = { login(email, contrasena) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006465))
                ) {
                    Text(text = "Ingresar", color = Color.White)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    modifier = Modifier.width(180.dp),
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006465))
                ) {
                    Text(text = "Soy hijo", color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = goToRegistrar) {
                    Text(text = "¿No tienes cuenta? ", color = Color(0xFF006465))
                    Text(text = "Regístrate", fontWeight = FontWeight.Bold, color = Color(0xFF006465))
                }
            }
        }
    }
}