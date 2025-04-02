package com.sagrd.spellingappv2.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sagrd.spellingappv2.presentation.navigation.Screen
import edu.ucne.spellingapp.R
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import com.sagrd.spellingappv2.presentation.login.AuthManager.logout

@Composable
fun NavDrawer(
    navHostController: NavHostController,
    isVisible: Boolean = false,
    onItemClick: (String) -> Unit,
    onClose: () -> Unit,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(
        initialValue = if (isVisible) DrawerValue.Open else DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()
    val selectedItem = remember { mutableStateOf(0) }
    val darkGradientColors = listOf(
        Color(0xFF0D47A1),
        Color(0xFF0277BD),
        Color(0xFF006064)
    )
    val lightGradientColors = listOf(
        Color(0xFF81D4FA),
        Color(0xFF4FC3F7),
        Color(0xFF80DEEA)
    )
    val isDarkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val gradientColors = if (isDarkTheme) darkGradientColors else lightGradientColors
    val selectedTextColor = if (isDarkTheme) Color.White else Color.Black

    var showConfirmationDialog by remember { mutableStateOf(false) }
    var pendingAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            drawerState.open()
        } else {
            drawerState.close()
        }
    }

    LaunchedEffect(drawerState.currentValue) {
        if (drawerState.currentValue == DrawerValue.Closed && isVisible) {
            onClose()
        }
    }

    val items = listOf(
        DrawerItem("Inicio", painterResource(R.drawable.home), Screen.Dashboard),
        DrawerItem("Perfil", painterResource(R.drawable.perfil3d), Screen.Perfil(0)),
        DrawerItem("Hijos", painterResource(R.drawable.hijo), Screen.HijoListScreen),
        DrawerItem("Pines", painterResource(R.drawable.codigos), Screen.PinListScreen),
        DrawerItem("Test", painterResource(R.drawable.probar), Screen.Dashboard),
        DrawerItem("Palabras", painterResource(R.drawable.palabras), Screen.Dashboard),
        DrawerItem("Estadisticas", painterResource(R.drawable.progreso), Screen.Dashboard),
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    items.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .then(
                                    if (index == selectedItem.value) {
                                        Modifier.background(
                                            brush = Brush.horizontalGradient(colors = gradientColors),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                    } else {
                                        Modifier.background(Color.Transparent)
                                    }
                                )
                                .padding(12.dp)
                                .clickable {
                                    selectedItem.value = index
                                    scope.launch {
                                        drawerState.close()
                                        onItemClick(item.title)
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = item.icon,
                                contentDescription = item.title,
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(4.dp),
                                contentScale = ContentScale.Fit
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = item.title,
                                color = if (index == selectedItem.value) selectedTextColor
                                else MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
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
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                ){
                    Text("Cerrar Sesión")
                }
            }
        },
        content = {
            content()
        }
    )
}




data class DrawerItem(
    val title: String,
    val icon: Painter,
    val route: Screen,
)

@Preview(showBackground = true)
@Composable
private fun previewNavDrawer() {
    val navHostController = rememberNavController()
    NavDrawer(
        navHostController = navHostController,
        isVisible = true,
        onItemClick = {
            navHostController.navigate(it)
        },
        onClose = {
            navHostController.navigateUp()
        },
        content = {
        }
    )
}