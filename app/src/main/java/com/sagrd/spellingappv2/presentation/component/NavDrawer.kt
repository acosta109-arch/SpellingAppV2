package com.sagrd.spellingappv2.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sagrd.spellingappv2.presentation.login.AuthManager
import com.sagrd.spellingappv2.presentation.navigation.Screen
import edu.ucne.spellingapp.R
import kotlinx.coroutines.launch

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
        DrawerItem("Perfil", painterResource(R.drawable.perfil3d), Screen.Dashboard),
        DrawerItem("Hijos", painterResource(R.drawable.hijos), Screen.HijoListScreen),
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
                        NavigationDrawerItem(
                            icon = {
                                Image(
                                    painter = item.icon,
                                    contentDescription = item.title,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(4.dp),
                                    contentScale = ContentScale.Fit
                                )
                            },
                            label = { Text(item.title) },
                            selected = index == selectedItem.value,
                            onClick = {
                                selectedItem.value = index
                                scope.launch {
                                    drawerState.close()
                                    onItemClick(item.title)
                                }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Button(

                    onClick = {
                        navHostController.navigate(Screen.LoginScreen) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                        scope.launch { drawerState.close() }
                        AuthManager.logout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C)),
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("Cerrar sesión", color = Color.White)
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
    val onLoginSuccess = {}
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