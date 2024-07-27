package com.sagrd.spellingappv2.ui.SplashScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sagrd.spellingappv2.R
import com.sagrd.spellingappv2.ui.Usuario.UsuarioViewModel
import com.sagrd.spellingappv2.util.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navHostController: NavHostController,
    viewModel : UsuarioViewModel = hiltViewModel()
) {
    val lista = viewModel.usuario.collectAsState(initial = emptyList())
    LaunchedEffect(key1 = true) {
        delay(4000)
        navHostController.popBackStack()
        if(lista.value.isEmpty())
            navHostController.navigate(Screen.MainScreen.route)
        else
            navHostController.navigate(Screen.ScoreScreen.route)
    }
    Splash()
}


@Composable
fun Splash() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
//            .background(
//                Brush.linearGradient(
//                    listOf(
//                        Teal200,
//                        Blue1,
//                        Teal200
//                    )
//                )
//            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,

        ) {
        Image(
            painter = painterResource(id = R.drawable.abeja),
            contentDescription = "Spelling App",
            Modifier.size(150.0.dp, 150.0.dp)
        )
    }

}