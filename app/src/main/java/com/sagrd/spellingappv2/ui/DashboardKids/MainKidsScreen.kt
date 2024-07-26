package com.sagrd.spellingappv2.ui.DashboardKids

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sagrd.spellingappv2.R
import com.sagrd.spellingappv2.ui.Usuario.UsuarioViewModel
import com.sagrd.spellingappv2.ui.practica.PracticaViewModel
import com.sagrd.spellingappv2.ui.theme.Blue1
import com.sagrd.spellingappv2.ui.theme.Teal200
import com.sagrd.spellingappv2.util.Screen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainKidsScreen(
    navHostController: NavHostController,
    viewModel: UsuarioViewModel = hiltViewModel(),
    usuarioId: Int?,
    viewModelPractica : PracticaViewModel = hiltViewModel()
) {
    //intentando recuperar el usuario
    val usuario = viewModel.Buscar(usuarioId ?: 0)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Dashboard",
                        fontFamily = FontFamily.Cursive,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colors.onPrimary
                    )

                },
                actions = {
                    Button(
                        onClick = { navHostController.navigate(Screen.WordQuery.route) },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Blue1,
                            contentColor = MaterialTheme.colors.onPrimary
                        )
                    )
                    {
                        Text(
                            text = "Words",
                            fontFamily = FontFamily.Cursive,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    /*IconButton(onClick = { navHostController.navigate(Screen.WordQuery.route) }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Words",
                            tint = MaterialTheme.colors.onPrimary

                        )
                    }*/
                },
                backgroundColor = Blue1,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModelPractica.Guardar(usuario.usuarioId)
                    navHostController.navigate(Screen.PracticaScreen.route + "/${1}")
                }
            ) {
                Text(
                    text = "Start",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Cursive
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Spacer(modifier = Modifier.padding(top = 20.dp))
            Image(
                ///Aqui va el logo de la qpp
                painter = painterResource(id = R.drawable.pngegg),
                contentDescription = "Spelling App",
                modifier = Modifier.size(width = 200.dp, height = 200.dp)
            )
            Spacer(modifier = Modifier.padding(top = 15.dp))
            Text(
                text = "Great you came back.",
                fontSize = 29.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Cursive
            )
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
                                    Teal200,
                                    Blue1
                                )
                            )
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    //en este text se mostrara el nombre del usuario que el elige
                    Text(
                        text = usuario.nombres,
                        fontSize = 50.sp,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Cursive
                    )
                }

            }
        }

    }
}