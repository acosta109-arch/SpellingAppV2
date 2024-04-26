package com.example.spellingappv2.ui.Palabra

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.spellingappv2.model.Palabra
import com.example.spellingappv2.ui.componentes.RowPalabra
import com.example.spellingappv2.ui.theme.Blue1
import com.example.spellingappv2.util.Screen

@Composable
fun WordQuery(
    navHostController: NavHostController,
    viewModel: WordViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Words",
                        fontFamily = FontFamily.Cursive,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                },
                backgroundColor = Blue1,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navHostController.navigate(Screen.WordRegister.route) },
                contentColor = contentColorFor(backgroundColor = Blue1),
                backgroundColor = Blue1,
            )
            {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White)
            }

        },
    ) {

        Column(
            modifier = Modifier
                .padding(it)
                .padding(8.dp)
        ) {

            val lis = viewModel.listado.collectAsState(initial = emptyList()).value
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(lis) { words ->
                    RowPalabra(
                        words,
                        onClick = {}
                    )
                }
            }
        }
    }
}

