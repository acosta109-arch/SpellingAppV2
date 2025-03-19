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
import androidx.navigation.compose.rememberNavController
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

                if (isLoggedIn) {
                    NavigationDrawer(
                        navHostController = navHost,
                        isLoggedIn = isLoggedIn,
                        onLoginSuccess = onLoginSuccess,
                        drawerState = drawerState
                    ) {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = { Text("SpellingApp") },
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
                    Scaffold {
                        Surface(modifier = Modifier.fillMaxSize().padding(it)) {
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
