package com.sagrd.spellingappv2.ui.main_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.navigation.NavHostController
import com.sagrd.spellingappv2.R
import com.sagrd.spellingappv2.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navHostController: NavHostController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Welcome to...",
                        fontSize = 35.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Cursive
                    )

                }
            )
        },

    ) {
        Column(
            modifier = Modifier.padding(it).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            Spacer(modifier = Modifier.padding(top = 20.dp))
            Image(
                painter = painterResource(id = R.drawable.pngegg),
                contentDescription = "Spelling App",
                modifier = Modifier.size(width = 200.dp, height = 200.dp)
            )
            Spacer(modifier = Modifier.padding(top = 20.dp))
            Text(
                text = "But..., \n" +
                        "what is Spelling Bee? ",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Cursive
            )
            Spacer(modifier = Modifier.padding(top = 15.dp))
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
            )
            {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Text(
                        text = "Is a competition in which children try to spell \n" +
                                "words correctly. Anyone who makes a mistake is \n" +
                                "out  and the competition continues \n" +
                                "until only one person is left.",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily.Cursive
                    )
                }

            }


            Spacer(modifier = Modifier.padding(top = 20.dp))
            Text(
                text = "Are you ready for to study? \n" +
                        "Register your first kid now",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Cursive
            )
            Button(
                onClick = { navHostController.navigate(Screen.RegistroUsuarioScreen.route) },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                contentPadding = PaddingValues(8.dp),
                shape = CutCornerShape(4.dp),
            ) {
                Text(
                    text = "Register Kid",
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp

                )
            }
        }
    }

}