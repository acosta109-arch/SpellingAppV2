package com.example.spellingappv2.ui.practica

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.spellingappv2.ui.Palabra.WordViewModel
import com.example.spellingappv2.ui.theme.Blue1
import com.example.spellingappv2.ui.theme.Yellow1
import com.example.spellingappv2.util.Screen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PracticaScreen(
    navHostController: NavHostController,
    viewModel: WordViewModel = hiltViewModel(),
    palabraId: Int? = 0,
    onSpeech:(String) -> Unit
    //speech : SpeechViewModel = hiltViewModel()
) {
    var palabra = viewModel.GetPalabra(palabraId ?: 0)
    val context = LocalContext.current
    var show by rememberSaveable { mutableStateOf(false) }

    //var speech : SpeechViewModel
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { show = true },
                contentColor = contentColorFor(backgroundColor = Blue1)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(palabra.imagenUrl)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.size(300.dp, 300.dp)
                )
                Text(
                    text = palabra.descripcion,
                    modifier = Modifier.size(300.dp, 70.dp),
                )
            }


            Button(
                onClick =
                {
                    navHostController.navigate(Screen.PracticaScreen.route + "/${palabra.palabraId + 1}")
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(horizontal = 20.dp, vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Blue1,
                    contentColor = MaterialTheme.colors.onPrimary
                ),
                //navHostController.navigate(Screen.PracticaScreen.route)
                //palabra = viewModel.GetPalabra(p)
                /*index += 1*/
            ) {
                Icon(imageVector = Icons.Default.ArrowRight, contentDescription = "", tint = MaterialTheme.colors.onPrimary)
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = palabra.palabra,
                modifier = Modifier.height(30.dp),
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(0.dp))
                IconButton(
                    onClick = { onSpeech(palabra.palabra)
                    }
                ) {
                    //ICON DE SONIDO (PREFERIBLEMENTE, UNA VOCINA)
                    Icon(imageVector = Icons.Default.MusicNote, contentDescription = "")
                }


            }
        }


        /*val lis = viewModel.listado.collectAsState(initial = emptyList()).value

        var palabra = viewModel.GetPalabra(palabras = lis)

        var index : Int = 0*/

    }
    DialogModel(
        show = show,
        { show = false },
        { show = false; navHostController.navigate(Screen.ResumenScreen.route) }
    )
}

@Composable
fun DialogModel(
    show: Boolean,
    onDismiss: () -> Unit,
    onAccept: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = { onDismiss() },

            confirmButton = {
                Button(
                    onClick = { onAccept() },
                    modifier = Modifier.safeContentPadding(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Green,
                        contentColor = Color.Black,
                    )
                ) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier.safeContentPadding(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red,
                        contentColor = Color.Black,
                    )
                ) {
                    Text(text = "No")
                }
            },
            title = {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.QuestionMark,
                        contentDescription = "",
                        tint = Color.Red,
                        modifier = Modifier.size(50.dp)
                    )
                }


            },
            text = {
                Text(
                    text = "Are you sure you want to stop practicing?",
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                )
            },
        )
    }
}

