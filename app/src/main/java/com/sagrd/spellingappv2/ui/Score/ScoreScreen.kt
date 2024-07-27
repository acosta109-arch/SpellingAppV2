package com.sagrd.spellingappv2.ui.Score

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sagrd.spellingappv2.ui.Usuario.UsuarioViewModel
import com.sagrd.spellingappv2.ui.componentes.RowUsuarios
import com.sagrd.spellingappv2.util.Screen
import com.sagrd.spellingappv2.R

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScoreScreen(
    navHostController: NavHostController,
    viewModel: UsuarioViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Welcome to...",
                        fontSize = 35.sp,
                        fontFamily = FontFamily.Cursive,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                    )
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navHostController.navigate(Screen.RegistroUsuarioScreen.route) },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimary
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
                //AQui va el logo de la app que como no lo tengo puse la abeja
                painter = painterResource(id = R.drawable.pngegg),
                contentDescription = "Spelling App",
                modifier = Modifier.size(width = 200.dp, height = 200.dp)
            )
            Spacer(modifier = Modifier.padding(top = 15.dp))
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .absolutePadding(right = 6.dp, left = 6.dp, bottom = 8.dp),
            )
            {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Text(
                        text = "Top Kids",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Cursive
                    )
                }

            }
            Spacer(modifier = Modifier.padding(top = 15.dp))
            Column(modifier = Modifier.fillMaxSize()) {
                val lista = viewModel.usuario.collectAsState(initial = emptyList())
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(lista.value) { user ->
                        RowUsuarios(
                            usuario = user,
                            onClick = {
                                navHostController.navigate(Screen.MainKidsScreen.route + "/${user.usuarioId}")
                            }
                        )
                    }
                }

                if (state.isLoading)
                    CircularProgressIndicator()
            }
        }
    }

}