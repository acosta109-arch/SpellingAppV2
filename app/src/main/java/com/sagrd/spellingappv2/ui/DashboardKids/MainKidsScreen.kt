package com.sagrd.spellingappv2.ui.DashboardKids

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sagrd.spellingappv2.ui.Usuario.UsuarioViewModel
import com.sagrd.spellingappv2.ui.practica.PracticaViewModel
import com.sagrd.spellingappv2.util.Screen
import com.sagrd.spellingappv2.R
import com.sagrd.spellingappv2.ui.Resumen.ElevatedElevatedCard

@OptIn(ExperimentalMaterial3Api::class)
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
                        fontWeight = FontWeight.ExtraBold
                    )

                },
                actions = {
                    Button(
                        onClick = { navHostController.navigate(Screen.WordQuery.route) }
                    )
                    {
                        Text(
                            text = "Words",
                            fontFamily = FontFamily.Cursive,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                },
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
                .padding(it)
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
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .absolutePadding(right = 6.dp, left = 6.dp, bottom = 8.dp),
            )
            {

                     Text(
                        text = usuario.nombres,
                        fontSize = 50.sp,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Cursive
                    )


            }
        }

    }
}