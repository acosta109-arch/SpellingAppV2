package com.sagrd.spellingappv2.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
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

    val isDarkMode = isSystemInDarkTheme()

    val appBarColor = if (isDarkMode) Color(0xFF283653) else Color(0xFF7FB3D5)

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

    val copiaContraseña = uiState.contrasena

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil", fontWeight = FontWeight.Bold, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = appBarColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = gradientColors
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                        .background(Color.White.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.size(80.dp),
                        tint = Color.White
                    )
                }
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(red = 190, green = 240, blue = 60, alpha = 255)),
                    modifier = Modifier
                        .padding(top = 16.dp)
                ) {
                    Text("Cambiar Foto")
                }
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text(text = "Nombre", color = Color.White) },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.White) },
                    value = uiState.nombre,
                    onValueChange = onNombreChange,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White.copy(alpha = 0.2f),
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White
                    ),
                    shape = RoundedCornerShape(4.dp)
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text(text = "Apellido", color = Color.White) },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.White) },
                    value = uiState.apellido,
                    onValueChange = onApellidoChange,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White.copy(alpha = 0.2f),
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White
                    ),
                    shape = RoundedCornerShape(4.dp)
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text(text = "Número De Teléfono", color = Color.White) },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Color.White) },
                    value = uiState.telefono,
                    onValueChange = onTelefonoChange,
                    visualTransformation = PhoneVisualTrans(), // Aplica el formato visualmente
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White.copy(alpha = 0.2f),
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White
                    ),
                    shape = RoundedCornerShape(4.dp)
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text(text = "Contraseña", color = Color.White) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White) },
                    value = uiState.contrasena,
                    onValueChange = onContrasenaChange,
                    visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White.copy(alpha = 0.2f),
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White
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

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text(text = "Confirmar Contraseña", color = Color.White) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White) },
                    value = copiaContraseña,
                    onValueChange = onConfirmarContrasenaChange,
                    visualTransformation = if (confirmarContrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White.copy(alpha = 0.2f),
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White
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
                            .border(2.dp, Color.White, CircleShape)
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

                Spacer(modifier = Modifier.padding(16.dp))
                Button(
                    onClick = { onEdit() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(red = 190, green = 240, blue = 60, alpha = 255)
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp)
                ) {
                    Text("Actualizar")
                }
                Spacer(modifier = Modifier.padding(32.dp))
            }
        }
    }
}

class PhoneVisualTrans : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter { it.isDigit() }

        val formatted = if (digits.isEmpty()) {
            ""
        } else {
            buildString {
                append("(")
                append(digits.take(3)) // Código de área
                if (digits.length > 3) append(") ")
                if (digits.length in 4..6) append(digits.substring(3))
                if (digits.length > 6) append(digits.substring(3, minOf(6, digits.length)) + "-")
                if (digits.length > 6) append(digits.substring(6, minOf(10, digits.length)))
            }
        }

        return TransformedText(AnnotatedString(formatted), PhoneOffsetMapping(digits, formatted))
    }
}


class PhoneOffsetMap(private val original: String, private val transformed: String) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        val digitsOnly = original.take(offset)
        var transformedOffset = 0
        var digitCount = 0

        for (char in transformed) {
            if (digitCount == digitsOnly.length) break
            transformedOffset++
            if (char.isDigit()) digitCount++
        }

        return transformedOffset
    }

    override fun transformedToOriginal(offset: Int): Int {
        var originalOffset = 0
        var digitCount = 0

        for (char in transformed) {
            if (digitCount == offset) break
            if (char.isDigit()) originalOffset++
            digitCount++
        }

        return originalOffset.coerceAtMost(original.length)
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
        onContrasenaChange = {},
        onConfirmarContrasenaChange = {},
        onFotoUrlChange = {}
    )
}