package com.sagrd.spellingappv2.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.sagrd.spellingappv2.presentation.login.UsuarioViewModel.UiState
import edu.ucne.spellingapp.R

@Composable
fun EditarPerfil(
    viewModel: UsuarioViewModel = hiltViewModel(),
    goBack: () -> Unit,
    onMenuClick: () -> Unit,
    usuarioId: Int,
) {
    LaunchedEffect(usuarioId) {
        viewModel.selectUsuario(usuarioId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    EditarPerfilBody(
        uiState = uiState,
        goBack = goBack,
        onDelete = viewModel::deleteUsuario,
        onMenuClick = onMenuClick,
        onEdit = viewModel::updateUsuario,
        onNombreChange = viewModel::onNombreChange,
        onApellidoChange = viewModel::onApellidoChange,
        onTelefonoChange = viewModel::onTelefonoChange,
        onEmailChange = viewModel::onEmailChange,
        onContrasenaChange = viewModel::onContrasenaChange,
        onConfirmarContrasenaChange = viewModel::onConfirmarContrasenaChange,
        onFotoUrlChange = viewModel::onFotoUrlChange,
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilBody(
    uiState: UiState,
    goBack: () -> Unit,
    onDelete: () -> Unit,
    onMenuClick: () -> Unit,
    onEdit: () -> Unit,
    onNombreChange: (String) -> Unit,
    onApellidoChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onConfirmarContrasenaChange: (String) -> Unit,
    onFotoUrlChange: (String) -> Unit,

    ) {
    val scrollState = rememberScrollState()
    var showImageDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var isImageValid by remember { mutableStateOf(false) }
    var contrasenaVisible by remember { mutableStateOf(false) }
    var confirmarContrasenaVisible by remember { mutableStateOf(false) }

    val imagePainter = rememberAsyncImagePainter(
        ImageRequest.Builder(context)
            .data(uiState.fotoUrl)
            .build()
    )

    isImageValid = imagePainter.state is AsyncImagePainter.State.Success

    // Matching the color scheme from LoginScreen
    val isDarkMode = isSystemInDarkTheme()
    val backgroundColor = if (isDarkMode) Color(0xFF489DA7) else Color(0xFF9DF0FB)
    val backgroundColorButton = Color(0xFF2B3132)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil", fontWeight = FontWeight.Bold) },
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
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                onClick = {},
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                Text("Cambiar Foto")
            }
            // Nombre Field
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text(text = "Nombre", color = Color.Black) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                value = uiState.nombre,
                onValueChange = onNombreChange,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                ),
                shape = RoundedCornerShape(4.dp)
            )

            // Apellido Field
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text(text = "Apellido", color = Color.Black) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                value = uiState.apellido,
                onValueChange = onApellidoChange,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                ),
                shape = RoundedCornerShape(4.dp)
            )

            // Telefono Field
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text(text = "Número De Teléfono", color = Color.Black) },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                value = uiState.telefono,
                onValueChange = onTelefonoChange,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                ),
                shape = RoundedCornerShape(4.dp)
            )

            // Contraseña Field
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text(text = "Contraseña", color = Color.Black) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                value = uiState.contrasena,
                onValueChange = onContrasenaChange,
                visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                ),
                shape = RoundedCornerShape(4.dp),
                trailingIcon = {
                    IconButton(onClick = { contrasenaVisible = !contrasenaVisible }) {
                        Image(
                            painter = painterResource(id = if (contrasenaVisible) R.drawable.ojo_abierto else R.drawable.ojo_cerrado),
                            contentDescription = "Mostrar/Ocultar Contraseña"
                        )
                    }
                }
            )

            // Confirmar Contraseña Field
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text(text = "Confirmar Contraseña", color = Color.Black) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                value = uiState.confirmarContrasena,
                onValueChange = onConfirmarContrasenaChange,
                visualTransformation = if (confirmarContrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                ),
                shape = RoundedCornerShape(4.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        confirmarContrasenaVisible = !confirmarContrasenaVisible
                    }) {
                        Image(
                            painter = painterResource(id = if (confirmarContrasenaVisible) R.drawable.ojo_abierto else R.drawable.ojo_cerrado),
                            contentDescription = "Mostrar/Ocultar Contraseña"
                        )
                    }
                }
            )

            if (uiState.fotoUrl.isNotEmpty() && isImageValid) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Black, CircleShape)
                        .clickable { showImageDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = imagePainter,
                        contentDescription = "Vista previa de foto",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Error and Success Messages
            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            uiState.successMessage?.let { message ->
                Text(
                    text = message,
                    color = Color.Green,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { onEdit() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(red = 190,green = 240, blue = 60,alpha = 255)
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                Text("Actualizar")
            }
        }
    }
}

@Preview
@Composable
private fun EditPreview() {
    EditarPerfilBody(
        uiState = UiState(),
        goBack = {},
        onDelete = {},
        onMenuClick = {},
        onEdit = {},
        onNombreChange = {},
        onApellidoChange = {},
        onTelefonoChange = {},
        onEmailChange = {},
        onContrasenaChange = {},
        onConfirmarContrasenaChange = {},
        onFotoUrlChange = {}
    )


}