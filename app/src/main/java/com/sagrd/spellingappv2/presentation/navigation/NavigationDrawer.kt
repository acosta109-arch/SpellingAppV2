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
        DrawerItem("Inicio", painterResource(R.drawable.home)),
        DrawerItem("Perfil", painterResource(R.drawable.perfil3d)),
        DrawerItem("Hijos", painterResource(R.drawable.hijos)),
        DrawerItem("Pines", painterResource(R.drawable.codigos)),
        DrawerItem("Test", painterResource(R.drawable.probar)),
        DrawerItem("Palabras", painterResource(R.drawable.palabras)),
        DrawerItem("Estadisticas", painterResource(R.drawable.progreso)),
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Título y elementos del drawer
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
                            onClick = { scope.launch { drawerState.close() } },

                        )
                        Divider()
                    }

                    // Espacio para empujar el botón hacia abajo
                    Spacer(modifier = Modifier.weight(1f))

                    // Cerrar sesión button
                    Button(
                        onClick = {
                            onLoginSuccess() // Simular la acción de cerrar sesión
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
    val icon: Painter
)

@Preview(showBackground = true)
@Composable
fun PreviewNavigationDrawer() {
    val drawerState = rememberDrawerState(DrawerValue.Open)
    val navHostController = rememberNavController()
    val onLoginSuccess = {}

    // Simulamos el contenido de la pantalla
    NavigationDrawer(
        navHostController = navHostController,
        isLoggedIn = true,
        onLoginSuccess = onLoginSuccess,
        drawerState = drawerState
    ) {
        // Aquí puedes agregar contenido de vista previa para el "content" que pasa como parámetro
        Text("Contenido principal de la app", modifier = Modifier.padding(16.dp))
    }
}
