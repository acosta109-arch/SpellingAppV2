package com.sagrd.spellingappv2.ui.Palabra

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.sagrd.spellingappv2.ui.componentes.RowPalabra
import com.sagrd.spellingappv2.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
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
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navHostController.navigate(Screen.WordRegister.route) },

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

