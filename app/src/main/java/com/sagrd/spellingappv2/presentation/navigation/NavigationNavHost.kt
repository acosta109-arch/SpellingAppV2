package com.sagrd.spellingappv2.presentation.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sagrd.spellingappv2.data.local.database.SpellingAppDb
import com.sagrd.spellingappv2.presentation.dashboard.DashboardScreen
import com.sagrd.spellingappv2.presentation.login.LoginScreen
import com.sagrd.spellingappv2.presentation.login.RegistrarScreen

@Composable
fun nav_spelling_app(spellingAppDb: SpellingAppDb, navHostController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
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
                }
            )
        }

        composable<Screen.Dashboard> {
            val args = it.toRoute<Screen.LoginScreen>()
            DashboardScreen(

            )
        }

        composable<Screen.RegistrarScreen> {
            val args = it.toRoute<Screen.LoginScreen>()
            RegistrarScreen(
                goBack = {
                    navHostController.navigateUp()
                }
            )
        }


    }
}