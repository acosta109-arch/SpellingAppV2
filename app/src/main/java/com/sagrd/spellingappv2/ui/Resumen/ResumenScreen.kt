package com.sagrd.spellingappv2.ui.Resumen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sagrd.spellingappv2.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
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
            )
        },
        floatingActionButton = {

            ExtendedFloatingActionButton(
                onClick = { navHostController.navigate(Screen.ScoreScreen.route)}
            ){
                Text("Exit")
            }
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Spacer(modifier = Modifier.padding(top = 30.dp))


                    Text(
                        text = "Achievements",
                        fontSize = 50.sp,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Cursive
                    )

            Spacer(modifier = Modifier.padding(top = 15.dp))

        }
    }
}

@Composable
fun ElevatedElevatedCard(modifier: Modifier, content: () -> Unit) {

}
