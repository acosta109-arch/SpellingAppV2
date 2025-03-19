package com.sagrd.spellingappv2

import NavigationDrawer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sagrd.spellingappv2.presentation.navigation.Screen
import com.sagrd.spellingappv2.presentation.navigation.nav_spelling_app
import com.sagrd.spellingappv2.ui.theme.SpellingAppV2Theme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpellingAppV2Theme {
                val navHost = rememberNavController()
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                var isLoggedIn by rememberSaveable { mutableStateOf(false) }
                val onLoginSuccess = { isLoggedIn = true }

                // Monitorear la ruta actual para cambiar el título
                val navBackStackEntry by navHost.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                // Determinar el título basado en la ruta actual
                val currentTitle = when {
                    currentDestination?.route?.contains("Dashboard") == true -> "Inicio"
                    currentDestination?.route?.contains("LoginScreen") == true -> "Iniciar Sesión"
                    currentDestination?.route?.contains("RegistrarScreen") == true -> "Registrarse"
                    currentDestination?.route?.contains("PinListScreen") == true -> "Lista Pines"
                    currentDestination?.route?.contains("PinScreen") == true -> "Agregar Pin"
                    else -> "SpellingApp"
                }

                if (isLoggedIn) {
                    NavigationDrawer(
                        navHostController = navHost,
                        isLoggedIn = isLoggedIn,
                        onLoginSuccess = {
                            isLoggedIn = false
                        },
                        drawerState = drawerState
                    ) {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = { Text(currentTitle) }, // Título dinámico basado en la ruta
                                    navigationIcon = {
                                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                                        }
                                    }
                                )
                            }
                        ) { paddingValues ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
                            ) {
                                nav_spelling_app(
                                    navHostController = navHost,
                                    onLoginSuccess = onLoginSuccess
                                )
                            }
                        }
                    }
                } else {
                    Scaffold(
                        topBar = {
                            // También mostrar el título dinámico en la pantalla de login
                            TopAppBar(
                                title = { Text(currentTitle) }
                            )
                        }
                    ) { paddingValues ->
                        Surface(modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)) {
                            nav_spelling_app(
                                navHostController = navHost,
                                onLoginSuccess = onLoginSuccess
                            )
                        }
                    }
                }
            }
        }
    }
}