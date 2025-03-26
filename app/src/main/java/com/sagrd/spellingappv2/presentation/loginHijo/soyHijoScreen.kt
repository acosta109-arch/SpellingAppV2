package com.sagrd.spellingappv2.presentation.loginHijo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.spellingapp.R

@Composable
fun LoginPinScreen(
    viewModel: LoginHijoViewModel = hiltViewModel(),
    goBack: () -> Unit,
    goToDashboard: () -> Unit,
    goToRegistrar: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isLoginSuccessful) {
        if (uiState.isLoginSuccessful) {
            goToDashboard()
            onLoginSuccess()
        }
    }

    LoginPinBodyScreen(
        uiState = uiState,
        onPinChange = viewModel::onPinChange,
        login = { pin ->
            viewModel.login()
        },
        goToRegistrar = goToRegistrar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPinBodyScreen(
    uiState: LoginUiState,
    onPinChange: (String) -> Unit,
    login: (String) -> Unit,
    goToRegistrar: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    var pinVisible by remember { mutableStateOf(false) }

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

    val backgroundColorLogin = Color(0xFF2B3132)
    val textColor = Color.White

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = gradientColors
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(4.dp, Color.Black, RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.abeja),
                contentDescription = "Logo",
                modifier = Modifier.size(80.dp)
            )

            Text(
                text = "Spelling App",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "Iniciar Sesión con PIN",
                fontSize = 18.sp,
                color = Color.Gray
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                label = { Text(text = "PIN", color = Color.Black) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                value = pin,
                onValueChange = {
                    pin = it
                    onPinChange(it)
                },
                visualTransformation = if (pinVisible) VisualTransformation.None else PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                ),
                shape = RoundedCornerShape(4.dp),
                trailingIcon = {
                    IconButton(onClick = { pinVisible = !pinVisible }) {
                        Image(
                            painter = painterResource(id = if (pinVisible) R.drawable.ojo_abierto else R.drawable.ojo_cerrado),
                            contentDescription = "Mostrar/Ocultar PIN"
                        )
                    }
                }
            )

            // Show error message if exists
            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = { login(pin) },
                colors = ButtonDefaults.elevatedButtonColors(containerColor = backgroundColorLogin),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(text = "Ingresar", color = Color.White)
            }

            // Loading indicator
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color.Gray
                )
                Text(
                    text = " O ",
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color.Gray
                )
            }

            TextButton(onClick = goToRegistrar) {
                Text("¿No tienes un PIN? Regístrate", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPinPreview() {
    LoginPinBodyScreen(
        uiState = LoginUiState(),
        onPinChange = {},
        login = {},
        goToRegistrar = {}
    )
}