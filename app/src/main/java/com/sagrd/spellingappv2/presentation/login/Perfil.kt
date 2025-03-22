package com.sagrd.spellingappv2.presentation.login

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sagrd.spellingappv2.presentation.login.UsuarioViewModel.UiState

@Composable
fun Perfil (
    viewModel: UsuarioViewModel = hiltViewModel(),
    goBack: () -> Unit,
    onMenuClick: () -> Unit,
    usuarioId: Int,
    goEdit: () -> Unit,
){
    LaunchedEffect(usuarioId) {
        viewModel.selectUsuario(usuarioId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PerfilBody(
        uiState = uiState,
        goBack = goBack,
        onDelete = viewModel::deleteUsuario,
        onMenuClick = onMenuClick,
        goEdit = goEdit
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilBody(
    uiState: UiState,
    goBack: () -> Unit,
    onDelete: () -> Unit,
    onMenuClick: () -> Unit,
    goEdit: () -> Unit,
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                ),
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                },

                )
        },
    ){innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Image(
                //     painter = painterResource(id = R.drawable.profile_image),
                //     contentDescription = "Foto de perfil",
                //     modifier = Modifier.fillMaxSize(),
                //     contentScale = ContentScale.Crop
                // )

                // Placeholder de icono para cuando no hay imagen
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Button(
                onClick = {goEdit()},
                colors = ButtonDefaults.buttonColors(containerColor = Color(red = 190, green = 240, blue = 60, alpha = 255)),
                modifier = Modifier
                    .padding(top = 16.dp)

            ){
                Text("Editar Perfil")
            }
            Card(
                colors = CardDefaults.cardColors(White),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .border(
                        1.dp,
                        color = Color.Black,
                        MaterialTheme.shapes.medium
                    )
                    .fillMaxWidth(),


            ) {
                Column(){
                    Text(
                        text = "Nombre",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 12.dp, top = 12.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "${uiState.nombre}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp,end = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }

            }

            Card(
                colors = CardDefaults.cardColors(White),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .border(
                        1.dp,
                        color = Color.Black,
                        MaterialTheme.shapes.medium
                    )
                    .fillMaxWidth()

            ) {
                Column(){
                    Text(
                        text = "Apellido",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 12.dp, top = 12.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "${uiState.apellido}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp,end = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Card(
                colors = CardDefaults.cardColors(White),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .border(
                        1.dp,
                        color = Color.Black,
                        MaterialTheme.shapes.medium
                    )
                    .fillMaxWidth()

            ) {
                Column(){
                    Text(
                        text = "Telefono",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 12.dp, top = 12.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "${uiState.telefono}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp,end = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(containerColor = Color(red = 240, green = 60, blue = 60, alpha = 255)),
                modifier = Modifier
                    .padding(top = 16.dp)

            ){
                Text("Cerrar Sesión")
            }

        }

    }

}

@Preview
@Composable
private fun PerfilPreview() {
    PerfilBody(
        uiState = UiState(
            nombre = "Juan",
            apellido = "Perez",
            telefono = "8291234567"
        ),
        goBack = {},
        onDelete = {},
        onMenuClick = {},
        goEdit = {}
    )
    
}