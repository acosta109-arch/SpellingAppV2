package com.sagrd.spellingappv2.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
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
    goToRegistrar: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            goToDashboard()
            onLoginSuccess()
        }
    }

    LoginBodyScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onContrasenaChange = viewModel::onContrasenaChange,
        login = { email, contrasena ->
            viewModel.login(email, contrasena)
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
    var contrasenaVisible by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF006465))
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Bienvenido/a",
                modifier = Modifier.padding(top = 10.dp),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFF666666)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.abeja),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(70.dp),
                contentScale = ContentScale.Fit
            )
        }

        Text(
            text = "Spelling App",
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = Color.Black
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF666666))
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Login",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color(0xFFBEEE3B)),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    label = { Text(text = "Correo Electrónico") },
                    value = email,
                    onValueChange = {
                        email = it
                        onEmailChange(it)
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(4.dp)
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    label = { Text(text = "Contraseña") },
                    value = contrasena,
                    onValueChange = {
                        contrasena = it
                        onContrasenaChange(it)
                    },
                    visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(4.dp),
                    trailingIcon = {
                        IconButton(onClick = { contrasenaVisible = !contrasenaVisible }) {
                            Image(
                                painter = painterResource(id = if (contrasenaVisible) R.drawable.ojo_abierto else R.drawable.ojo_cerrado),
                                contentDescription = "Mostrar/Ocultar Contraseña"
                            )
                        }
                    }
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { login(email, contrasena) },
                        modifier = Modifier
                            .width(200.dp)
                            .padding(bottom = 12.dp)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006465)),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(text = "Ingresar", color = Color.White)
                    }

                    Button(
                        onClick = { },
                        modifier = Modifier
                            .width(200.dp)
                            .padding(bottom = 16.dp)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006465)),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(text = "Soy hijo", color = Color.White)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "¿No tienes cuenta? ",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Regístrate",
                        color = Color(0xFF006465),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { goToRegistrar() }
                    )
                }

            }
        }
    }
}