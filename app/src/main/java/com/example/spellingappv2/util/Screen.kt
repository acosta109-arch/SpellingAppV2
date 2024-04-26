package com.example.spellingappv2.util

sealed class Screen (val route :String){
    object SplashScreen : Screen("Splash")
    object MainScreen : Screen("MainScreen")
    object RegistroUsuarioScreen: Screen("RegistroUsuario")
    object WordRegister: Screen("NavegarRegistro")
    object WordQuery: Screen("NavegarConsulta")
    object ScoreScreen : Screen("DashboardKid")
    object MainKidsScreen : Screen("KidsMain")
    object PracticaScreen : Screen("Practica")
    object ResumenScreen : Screen("ResumenScreen")
}