package com.example.spellingappv2.ui.MainScreen

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
import com.example.spellingappv2.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.spellingappv2.ui.theme.Blue1
import com.example.spellingappv2.ui.theme.Yellow1
import com.example.spellingappv2.ui.theme.Yellow2
import com.example.spellingappv2.util.Screen

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

                },
                backgroundColor = Blue1,
            )
        },

    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
//            Text(
//                text = "Welcome to...",
//                fontSize = 35.sp,
//                fontWeight = FontWeight.ExtraBold,
//                fontFamily = FontFamily.Cursive
//            )
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .absolutePadding(right = 6.dp, left = 6.dp, bottom = 8.dp),
                backgroundColor = Color.Transparent,
                shape = CutCornerShape(6.dp)
            )
            {
                Column(
                    modifier = Modifier
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Yellow2,
                                    Yellow1,
                                )
                            )
                        ),
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
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Blue1,
                    contentColor = Color.Black,
                ),
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