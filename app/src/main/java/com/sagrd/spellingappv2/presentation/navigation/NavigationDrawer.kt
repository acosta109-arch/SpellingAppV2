import android.R.attr.divider
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import edu.ucne.spellingapp.R
import androidx.navigation.NavHostController
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.navigation.compose.rememberNavController
import com.sagrd.spellingappv2.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    navHostController: NavHostController,
    isLoggedIn: Boolean,
    onLoginSuccess: () -> Unit,
    drawerState: DrawerState,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val items = listOf(
        DrawerItem("Inicio", painterResource(R.drawable.home), Screen.Dashboard),
        DrawerItem("Perfil", painterResource(R.drawable.perfil3d), Screen.Dashboard), // Puedes cambiar a perfil cuando lo tengas
        DrawerItem("Hijos", painterResource(R.drawable.hijos), Screen.Dashboard), // Puedes cambiar a hijos cuando lo tengas
        DrawerItem("Pines", painterResource(R.drawable.codigos), Screen.PinListScreen),
        DrawerItem("Test", painterResource(R.drawable.probar), Screen.Dashboard), // Puedes cambiar a test cuando lo tengas
        DrawerItem("Palabras", painterResource(R.drawable.palabras), Screen.Dashboard), // Puedes cambiar a palabras cuando lo tengas
        DrawerItem("Estadisticas", painterResource(R.drawable.progreso), Screen.Dashboard), // Puedes cambiar a estadísticas cuando lo tengas
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("SpellingApp", modifier = Modifier.padding(16.dp))
                    Divider()

                    items.forEach { item ->
                        NavigationDrawerItem(
                            icon = {
                                Image(
                                    painter = item.icon,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .padding(4.dp),
                                    contentScale = ContentScale.Fit
                                )
                            },
                            label = { Text(item.title) },
                            selected = false,
                            onClick = {
                                navHostController.navigate(item.route)
                                scope.launch { drawerState.close() }
                            },
                        )
                        Divider()
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            onLoginSuccess()
                            navHostController.navigate(Screen.LoginScreen) {
                                popUpTo(0) {
                                    inclusive = true
                                }
                            }
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Cerrar sesión")
                    }
                }
            }
        },
        content = content
    )
}

data class DrawerItem(
    val title: String,
    val icon: Painter,
    val route: Screen
)

@Preview(showBackground = true)
@Composable
fun PreviewNavigationDrawer() {
    val drawerState = rememberDrawerState(DrawerValue.Open)
    val navHostController = rememberNavController()
    val onLoginSuccess = {}

    NavigationDrawer(
        navHostController = navHostController,
        isLoggedIn = true,
        onLoginSuccess = onLoginSuccess,
        drawerState = drawerState
    ) {
    }
}