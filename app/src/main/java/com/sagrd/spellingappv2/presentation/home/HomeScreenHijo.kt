import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.sagrd.spellingappv2.presentation.component.NavDrawerHijo
import com.sagrd.spellingappv2.presentation.navigation.Screen
import edu.ucne.spellingapp.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun HomeScreenHijo(
    navHostController: NavHostController
) {
    var isDrawerVisible by remember { mutableStateOf(false) }
    var showHelpModal by remember { mutableStateOf(false) }
    val context = LocalContext.current

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

    val textGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF3498DB),
            Color(0xFF2E86C1),
            Color(0xFF1A5276)
        )
    )

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

                Button(
                    onClick = { showHelpModal = true },
                    modifier = Modifier
                        .padding(bottom = 24.dp, start = 24.dp, end = 24.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "¿Necesitas ayuda?",
                        style = TextStyle(
                            brush = textGradient,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            if (showHelpModal) {
                Dialog(
                    onDismissRequest = { showHelpModal = false },
                    properties = DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .widthIn(max = 320.dp)
                                .padding(16.dp)
                                .animateContentSize(),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.abeja),
                                    contentDescription = "Abeja ayudante",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(bottom = 16.dp)
                                        .clip(CircleShape)
                                )

                                Text(
                                    text = "¿No sabes usar la app?",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        textAlign = TextAlign.Center
                                    ),
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW,
                                            Uri.parse("https://www.flipsnack.com/CFA9A6BBDC9/gu-a-de-usuario/full-view.html"))
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF5499C7)
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                ) {
                                    Text(
                                        text = "Ir a guía",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.White
                                        ),
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}