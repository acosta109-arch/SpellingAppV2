package com.sagrd.spellingappv2.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import edu.ucne.spellingapp.R

@Composable
fun RegistrarScreen(viewModel: UsuarioViewModel = hiltViewModel(), goBack: () -> Unit) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    RegistrarBodyScreen(
        uiState = uiState.value,
        onNombreChange = viewModel::onNombreChange,
        onApellidoChange = viewModel::onApellidoChange,
        onTelefonoChange = viewModel::onTelefonoChange,
        onEmailChange = viewModel::onEmailChange,
        onContrasenaChange = viewModel::onContrasenaChange,
        onConfirmarContrasenaChange = viewModel::onConfirmarContrasenaChange,
        onFotoUrlChange = viewModel::onFotoUrlChange,
        save = viewModel::saveUsuario,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarBodyScreen(
    uiState: UsuarioViewModel.UiState,
    onNombreChange: (String) -> Unit,
    onApellidoChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onConfirmarContrasenaChange: (String) -> Unit,
    onFotoUrlChange: (String) -> Unit,
    save: () -> Unit,
    goBack: () -> Unit
) {
    val tealColor = Color(0xFF006465)
    val limeGreenColor = Color(0xFFBBF24B)
    val grayTextColor = Color(0xFF4A4A4A)
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Surface(
            color = tealColor,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = "Bienvenido/a",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF666666)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.abeja),
                        contentDescription = "Logo",
                        modifier = Modifier.size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Spelling App",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Surface(
                color = Color(0xFF666666),
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Registrarse",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(limeGreenColor)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val textFieldModifier = Modifier.fillMaxWidth()
                val textFieldColors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )

                OutlinedTextField(
                    modifier = textFieldModifier,
                    value = uiState.nombre,
                    onValueChange = onNombreChange,
                    label = { Text("Nombre", color = grayTextColor) },
                    singleLine = true,
                    colors = textFieldColors
                )

                OutlinedTextField(
                    modifier = textFieldModifier,
                    value = uiState.apellido,
                    onValueChange = onApellidoChange,
                    label = { Text("Apellido", color = grayTextColor) },
                    singleLine = true,
                    colors = textFieldColors
                )

                OutlinedTextField(
                    modifier = textFieldModifier,
                    value = uiState.telefono,
                    onValueChange = onTelefonoChange,
                    label = { Text("Número De Teléfono", color = grayTextColor) },
                    singleLine = true,
                    colors = textFieldColors
                )

                OutlinedTextField(
                    modifier = textFieldModifier,
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    label = { Text("Correo Electrónico", color = grayTextColor) },
                    singleLine = true,
                    colors = textFieldColors
                )

                OutlinedTextField(
                    modifier = textFieldModifier,
                    value = uiState.contrasena,
                    onValueChange = onContrasenaChange,
                    label = { Text("Contraseña", color = grayTextColor) },
                    singleLine = true,
                    visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = textFieldColors,
                    trailingIcon = {
                        IconButton(onClick = { contrasenaVisible = !contrasenaVisible }) {
                            Icon(
                                imageVector = if (contrasenaVisible) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                contentDescription = "Mostrar/Ocultar Contraseña"
                            )
                        }
                    }
                )

                OutlinedTextField(
                    modifier = textFieldModifier,
                    value = uiState.confirmarContrasena,
                    onValueChange = onConfirmarContrasenaChange,
                    label = { Text("Confirmar Contraseña", color = grayTextColor) },
                    singleLine = true,
                    visualTransformation = if (confirmarContrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = textFieldColors,
                    trailingIcon = {
                        IconButton(onClick = { confirmarContrasenaVisible = !confirmarContrasenaVisible }) {
                            Icon(
                                imageVector = if (confirmarContrasenaVisible) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                contentDescription = "Mostrar/Ocultar Confirmar Contraseña"
                            )
                        }
                    }
                )

                OutlinedTextField(
                    modifier = textFieldModifier,
                    value = uiState.fotoUrl,
                    onValueChange = onFotoUrlChange,
                    label = { Text("URL de Foto", color = grayTextColor) },
                    singleLine = true,
                    colors = textFieldColors,
                    trailingIcon = {
                        if (uiState.fotoUrl.isNotEmpty()) {
                            Button(
                                onClick = { showImageDialog = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = tealColor
                                ),
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Ver foto",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.size(4.dp))
                                    Text(
                                        text = "Vista previa",
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                )

                if (uiState.fotoUrl.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.White)
                            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                            .padding(4.dp)
                            .clickable { showImageDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isImageValid) {
                            Image(
                                painter = imagePainter,
                                contentDescription = "Vista previa de foto",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Text(
                                text = "URL de imagen no válida o en carga",
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {save() },
                        modifier = Modifier
                            .width(200.dp)
                            .padding(bottom = 16.dp)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006465)),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(text = "Registrar", color = Color.White)
                    }
                }

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(onClick = goBack) {
                        Text(
                            text = "¿Ya tienes cuenta? Iniciar sesión",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    }
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

                uiState.errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showImageDialog && uiState.fotoUrl.isNotEmpty()) {
        Dialog(onDismissRequest = { showImageDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(450.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
            ) {
                if (isImageValid) {
                    Image(
                        painter = imagePainter,
                        contentDescription = "Vista previa",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se pudo cargar la imagen",
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                IconButton(
                    onClick = { showImageDialog = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(36.dp)
                        .background(Color(0x80000000), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = Color.White
                    )
                }
            }
        }
    }
}