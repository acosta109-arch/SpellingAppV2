package com.sagrd.spellingappv2.presentation.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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
    val context = LocalContext.current

    // Check if user is already logged in
    LaunchedEffect(Unit) {
        if (AuthManager.isLoggedIn) {
            goToDashboard()
            onLoginSuccess()
        }
    }

    // Configure Google Sign In
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("617138256064-jc5tu8he582ta80gj4s2gbvataddr3hb.apps.googleusercontent.com") // Replace with your actual web client ID
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.handleGoogleSignInResult(result.data)
        }
    }

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
        goToRegistrar = goToRegistrar,
        onGoogleSignInClick = {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginBodyScreen(
    uiState: UsuarioViewModel.UiState,
    onEmailChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    login: (String, String) -> Unit,
    goToRegistrar: () -> Unit,
    onGoogleSignInClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var contrasenaVisible by remember { mutableStateOf(false) }

    val isDarkMode = isSystemInDarkTheme()
    val backgroundColor = if (isDarkMode) Color(0xFF489DA7) else Color(0xFF9DF0FB)
    val backgroundColorLogin = Color(0xFF2B3132)
    val textColor = Color.White
    val buttonColor = Color(0xFF006465)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
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

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                label = { Text(text = "Correo Electrónico", color = Color.Black) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
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
                label = { Text(text = "Contraseña", color = Color.Black) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
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

            // Show error message if exists
            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = { login(email, contrasena) },
                colors = ButtonDefaults.elevatedButtonColors(containerColor = backgroundColorLogin),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(text = "Ingresar", color = Color.White)
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

            // In LoginScreen.kt
            Button(
                onClick = onGoogleSignInClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "Google logo",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Continuar con Google",
                        color = Color.Black
                    )
                }
            }

            TextButton(onClick = goToRegistrar) {
                Text("¿No tienes cuenta? Regístrate", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            // Loading indicator
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    LoginBodyScreen(
        uiState = UsuarioViewModel.UiState(),
        onEmailChange = {},
        onContrasenaChange = {},
        login = { _, _ -> },
        goToRegistrar = {},
        onGoogleSignInClick = {}
    )
}