package com.sagrd.spellingappv2.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.sagrd.spellingappv2.presentation.login.AuthManager.logout
import com.sagrd.spellingappv2.presentation.login.LoginUiState
import com.sagrd.spellingappv2.presentation.navigation.Screen
import edu.ucne.spellingapp.R

@Composable
fun Perfil(
    usuarioId: Int,
    navHostController: NavHostController,
    viewModel: UsuarioViewModel = hiltViewModel(),
    onMenuClick: () -> Unit,
    goEdit: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(usuarioId) {
            viewModel.selectUsuario(usuarioId = usuarioId)
    }

    PerfilBody(
        navHostController = navHostController,
        uiState = uiState,
        onMenuClick = onMenuClick,
        goEdit = goEdit
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilBody(
    navHostController: NavHostController,
    uiState: LoginUiState,
    onMenuClick: () -> Unit,
    goEdit: (Int) -> Unit,
){
    val isDarkMode = isSystemInDarkTheme()
    val appBarColor = if (isDarkMode) Color(0xFF283653) else Color(0xFF7FB3D5)
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

    var showConfirmationDialog by remember { mutableStateOf(false) }
    var pendingAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.Bold, color = Color.White) },
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
    ){ innerPadding ->
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
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                        .background(Color.White.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.size(80.dp),
                        tint = Color.White
                    )
                }
                Button(
                    onClick = { goEdit(uiState.usuarioActual?.usuarioId ?: 0) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(red = 190, green = 240, blue = 60, alpha = 255)),
                    modifier = Modifier
                        .padding(top = 16.dp)

                ){
                    Text("Editar Perfil")
                }
                Card(
                    colors = CardDefaults.cardColors(White),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .border(
                            1.dp,
                            color = Color.Black,
                            MaterialTheme.shapes.medium
                        )
                        .fillMaxWidth(),
                ) {
                    Column(){
                        Text(
                            text = "Nombre",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 12.dp, top = 12.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = uiState.usuarioActual?.nombre ?: "no encontrado",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp,end = 12.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(White),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .border(
                            1.dp,
                            color = Color.Black,
                            MaterialTheme.shapes.medium
                        )
                        .fillMaxWidth()
                ) {
                    Column(){
                        Text(
                            text = "Apellido",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 12.dp, top = 12.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = uiState.usuarioActual?.apellido?: "no encontrado",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp,end = 12.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(White),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .border(
                            1.dp,
                            color = Color.Black,
                            MaterialTheme.shapes.medium
                        )
                        .fillMaxWidth()
                ) {
                    Column(){
                        Text(
                            text = "Telefono",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 12.dp, top = 12.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = formatPhoneNumber(uiState.usuarioActual?.telefono),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp,end = 12.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                if (showConfirmationDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showConfirmationDialog = false
                            pendingAction = null
                        },
                        title = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.abeja),
                                    contentDescription = "Abeja",
                                    modifier = Modifier
                                        .size(80.dp)
                                        .padding(bottom = 16.dp)
                                )
                                Text(
                                    "Cerrar Sesión",
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        },
                        text = { Text("¿Estás seguro que deseas cerrar sesión?") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    pendingAction?.invoke()
                                    showConfirmationDialog = false
                                    pendingAction = null
                                }
                            ) {
                                Text("Confirmar")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showConfirmationDialog = false
                                    pendingAction = null
                                }
                            ) {
                                Text("Cancelar")
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        pendingAction = {
                            navHostController.navigate(Screen.LoginScreen) {
                                popUpTo(0) {
                                    inclusive = true
                                }
                            }
                            logout()
                        }
                        showConfirmationDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(
                        red = 183,
                        green = 28,
                        blue = 28,
                        alpha = 255
                    )
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp)
                ){
                    Text("Cerrar Sesión")
                }
            }
        }
    }
}

fun formatPhoneNumber(phoneNumber: String?): String {
    val digitsOnly = phoneNumber?.replace("\\D".toRegex(), "") ?: return ""

    return if (digitsOnly.length >= 10) {
        val areaCode = digitsOnly.substring(0, 3)
        val middleThree = digitsOnly.substring(3, 6)
        val lastFour = digitsOnly.substring(6, 10)
        "($areaCode) $middleThree-$lastFour"
    } else {
        digitsOnly
    }
}

@Preview
@Composable
private fun pelfilpreview() {
    PerfilBody(
        navHostController = NavHostController(LocalContext.current),
        uiState = LoginUiState(),
        onMenuClick = {},
        goEdit = {},
    )
}
