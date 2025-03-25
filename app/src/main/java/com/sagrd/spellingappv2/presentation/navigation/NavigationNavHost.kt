package com.sagrd.spellingappv2.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.derivedStateOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.sagrd.spellingappv2.presentation.component.NavDrawer
import com.sagrd.spellingappv2.presentation.dashboard.DashboardScreen
import com.sagrd.spellingappv2.presentation.examTest.TestScreen
import com.sagrd.spellingappv2.presentation.hijos.HijoDelete
import com.sagrd.spellingappv2.presentation.hijos.HijosEditScreen
import com.sagrd.spellingappv2.presentation.hijos.HijosListScreen
import com.sagrd.spellingappv2.presentation.hijos.HijosScreen
import com.sagrd.spellingappv2.presentation.login.EditarPerfil
import com.sagrd.spellingappv2.presentation.login.LoginScreen
import com.sagrd.spellingappv2.presentation.login.Perfil
import com.sagrd.spellingappv2.presentation.login.RegistrarScreen
import com.sagrd.spellingappv2.presentation.palabras.PalabrasListScreen
import com.sagrd.spellingappv2.presentation.pin.PinDelete
import com.sagrd.spellingappv2.presentation.pin.PinScreen
import edu.ucne.registrotecnicos.presentation.pin.PinListScreen

@Composable
fun nav_spelling_app(
    navHostController: NavHostController,
    onLoginSuccess: () -> Unit,
) {
    val isDrawerVisible = remember { mutableStateOf(false) }

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    val shouldShowDrawer by remember(currentRoute) {
        derivedStateOf {
            !currentRoute.contains("LoginScreen") && !currentRoute.contains("RegistrarScreen")
        }
    }

    val showDrawer = {
        if (shouldShowDrawer) {
            isDrawerVisible.value = true
        }
    }

    val closeDrawer = {
        isDrawerVisible.value = false
    }

    if (shouldShowDrawer) {
        NavDrawer(
            isVisible = isDrawerVisible.value,
            navHostController = navHostController,
            onItemClick = { itemTitle ->
                when (itemTitle) {
                    "Inicio" -> navHostController.navigate(Screen.Dashboard)
                    "Perfil" -> navHostController.navigate(Screen.Perfil(0))
                    "Hijos" -> navHostController.navigate(Screen.HijoListScreen)
                    "Pines" -> navHostController.navigate(Screen.PinListScreen)
                    "Test" -> navHostController.navigate(Screen.TestScreen)
                    "Palabras" -> navHostController.navigate(Screen.PalabrasListScreen)
                    "Estadisticas" -> navHostController.navigate(Screen.Dashboard)
                }
                closeDrawer()
            },
            onClose = closeDrawer
        ) {
            NavContent(navHostController, onLoginSuccess, showDrawer)
        }
    } else {
        NavContent(navHostController, onLoginSuccess, showDrawer)
    }
}

@Composable
private fun NavContent(
    navHostController: NavHostController,
    onLoginSuccess: () -> Unit,
    onMenuClick: () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.LoginScreen
    ) {
        composable<Screen.LoginScreen> {
            LoginScreen(
                goToDashboard = {
                    navHostController.navigate(Screen.Dashboard)
                },
                goBack = {
                    navHostController.navigateUp()
                },
                goToRegistrar = {
                    navHostController.navigate(Screen.RegistrarScreen)
                },
                onLoginSuccess = onLoginSuccess
            )
        }

        composable<Screen.Dashboard> {
            DashboardScreen(
                onMenuClick = onMenuClick
            )
        }

        composable<Screen.RegistrarScreen> {
            RegistrarScreen(
                goBack = {
                    navHostController.navigateUp()
                },
            )
        }

        composable<Screen.PinListScreen> {
            PinListScreen(
                onCreate = { navHostController.navigate(Screen.PinScreen(0)) },
                onDelete = { navHostController.navigate(Screen.PinDelete(it)) },
                onBack = { navHostController.navigateUp() },
                onMenuClick = onMenuClick
            )
        }

        composable<Screen.PinScreen> {
            PinScreen(
                goBack = {
                    navHostController.navigateUp()
                },
                onMenuClick = onMenuClick
            )
        }

        composable<Screen.PinDelete> {
            val args = it.toRoute<Screen.PinScreen>()
            PinDelete(
                pinId = args.pinId,
                goBack = {
                    navHostController.navigateUp()
                },
                onMenuClick = onMenuClick
            )
        }

        composable<Screen.HijoListScreen> {
            HijosListScreen(
                onCreate = { navHostController.navigate(Screen.HijoScreen(0)) },
                onDelete = { navHostController.navigate(Screen.HijoDelete(it)) },
                onBack = { navHostController.navigateUp() },
                onEdit = { navHostController.navigate(Screen.HijoEdit(it)) },
                onMenuClick = onMenuClick
            )
        }

        composable<Screen.HijoScreen> {
            HijosScreen(
                goBack = {
                    navHostController.navigateUp()
                },
                onMenuClick = onMenuClick
            )
        }

        composable<Screen.HijoDelete> {
            val args = it.toRoute<Screen.HijoDelete>()
            HijoDelete(
                hijoId = args.hijoId,
                goBack = {
                    navHostController.navigateUp()
                },
                onMenuClick = onMenuClick

            )
        }

        composable<Screen.HijoEdit> {
            val args = it.toRoute<Screen.HijoEdit>()
            HijosEditScreen(
                hijoId = args.hijoId,
                goBack = {
                    navHostController.navigateUp()
                },
                onMenuClick = onMenuClick
            )
        }

        composable<Screen.Perfil> {
            val args = it.toRoute<Screen.Perfil>()
            Perfil(
                usuarioId = args.usuarioId,
                goBack = {
                    navHostController.navigateUp()
                },
                onMenuClick = onMenuClick,
                goEdit = { navHostController.navigate(Screen.EditPerfil(args.usuarioId)) },
                navHostController = navHostController
            )
        }

        composable<Screen.EditPerfil> {
            val args = it.toRoute<Screen.EditPerfil>()
            EditarPerfil(
                usuarioId = args.UsuarioId,
                goBack = {
                    navHostController.navigateUp()
                    },
                onMenuClick = onMenuClick
            )
        }

        composable<Screen.PalabrasListScreen> {
            PalabrasListScreen(
                onBack = {
                    navHostController.navigateUp()
                }
            )
        }

        composable<Screen.TestScreen> {
            TestScreen(
                onBack = {
                    navHostController.navigateUp()
                }
            )
        }
    }
}