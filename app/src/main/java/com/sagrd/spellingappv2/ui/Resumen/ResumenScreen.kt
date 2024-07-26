package com.sagrd.spellingappv2.ui.Resumen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sagrd.spellingappv2.ui.theme.Blue1
import com.sagrd.spellingappv2.ui.theme.Teal200
import com.sagrd.spellingappv2.util.Screen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ResumenScreen(navHostController: NavHostController) {
    //val state = viewModel.state.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Review Practice",
                        fontSize = 35.sp,
                        fontFamily = FontFamily.Cursive,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                    )

                },
                backgroundColor = Blue1,
            )
        },
        floatingActionButton = {

            ExtendedFloatingActionButton(
                text = { Text("Exit") },
                onClick = { navHostController.navigate(Screen.ScoreScreen.route)},
                backgroundColor = Blue1,
                contentColor = Color.Black
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Spacer(modifier = Modifier.padding(top = 30.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .absolutePadding(right = 6.dp, left = 6.dp, bottom = 8.dp),
                backgroundColor = Color.Transparent,
                shape = CutCornerShape(10.dp)
            )
            {
                Column(
                    modifier = Modifier
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Blue1,
                                    Teal200
                                )
                            )
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Text(
                        text = "Achievements",
                        fontSize = 50.sp,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Cursive
                    )
                }

            }
            Spacer(modifier = Modifier.padding(top = 15.dp))
            Column(modifier = Modifier.fillMaxSize()) {
                //val lista = viewModel.usuario.collectAsState(initial = emptyList())
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    /* items(lista.value) { user ->
                         RowResumen(
                             usuario = user,
                             onClick = {
                                 navHostController.navigate(Screen.MainKidsScreen.route + "/${user.usuarioId}")
                             }
                         )
                     }*/
                }

//                if (state.isLoading)
//                    CircularProgressIndicator()
            }
        }
    }
}