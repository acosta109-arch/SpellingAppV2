package com.sagrd.spellingappv2.presentation.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sagrd.spellingappv2.presentation.component.NavDrawerHijo
import com.sagrd.spellingappv2.presentation.navigation.Screen
import edu.ucne.spellingapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenHijo(
    navHostController: NavHostController
) {
    var isDrawerVisible by remember { mutableStateOf(false) }

    val isDarkMode = isSystemInDarkTheme()

    val gradientColors = if (isDarkMode) {
        listOf(
            Color(0xFF283653),
            Color(0xFF003D42),
            Color(0xFF177882)
        )
    } else {
        listOf(
            Color(0xFF7FB3D5),
            Color(0xFF76D7EA),
            Color(0xFFAED6F1)
        )
    }

    val welcomeBoxGradient = if (isDarkMode) {
        listOf(
            Color(0xFF1F2B42),
            Color(0xFF0A5159)
        )
    } else {
        listOf(
            Color(0xFF5499C7),
            Color(0xFF45B7D1)
        )
    }

    val appBarColor = if (isDarkMode) Color(0xFF283653) else Color(0xFF7FB3D5)

    NavDrawerHijo(
        navHostController = navHostController,
        isVisible = isDrawerVisible,
        onItemClick = { itemTitle ->
            when (itemTitle) {
                "Inicio" -> navHostController.navigate(Screen.DashboardHijo)
                "Aprender" -> navHostController.navigate(Screen.AprenderScreen)
            }
            isDrawerVisible = false
        },
        onClose = { isDrawerVisible = false }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Menú Principal") },
                    navigationIcon = {
                        IconButton(onClick = {
                            isDrawerVisible = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Abrir Menú"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = appBarColor,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(
                        Brush.verticalGradient(
                            colors = gradientColors
                        )
                    ),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                colors = welcomeBoxGradient
                            )
                        )
                        .padding(vertical = 16.dp, horizontal = 24.dp)
                ) {
                    Text(
                        text = "Bienvenido Niño/a 👋",
                        style = TextStyle(
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.kids),
                        contentDescription = "Imagen de niños",
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .aspectRatio(1.2f)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview
@Composable
fun PreviewDashboardHijoScreen() {
    val navController = rememberNavController()
    HomeScreenHijo(navHostController = navController)
}