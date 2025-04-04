package com.sagrd.spellingappv2.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        save = viewModel::saveUsuario,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarBodyScreen(
    uiState: UsuarioViewModel.LoginUiState,
    onNombreChange: (String) -> Unit,
    onApellidoChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onConfirmarContrasenaChange: (String) -> Unit,
    save: () -> Unit,
    goBack: () -> Unit
) {
    val scrollState = rememberScrollState()
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

    val backgroundColorButton = Color(0xFF2B3132)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = gradientColors
                )
            )
            .verticalScroll(scrollState),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(4.dp, Color.Black, RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.abeja),
                contentDescription = "Logo",
                modifier = Modifier.size(80.dp)
            )

            Text(
                text = "Spelling App",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "Registrarse",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
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
                ,singleLine = true
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
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
                ,singleLine = true
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                label = { Text(text = "Número De Teléfono", color = Color.Black) },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                value = uiState.telefono,
                onValueChange = { newValue ->
                    val digits = newValue.filter { it.isDigit() }.take(10)
                    onTelefonoChange(digits)
                },
                visualTransformation = PhoneVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                ),
                shape = RoundedCornerShape(4.dp)
                ,singleLine = true
            )


            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                label = { Text(text = "Correo Electrónico", color = Color.Black) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                value = uiState.email,
                onValueChange = onEmailChange,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                ),
                shape = RoundedCornerShape(4.dp)
                ,singleLine = true
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
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
                singleLine = true,
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
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
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
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { confirmarContrasenaVisible = !confirmarContrasenaVisible }) {
                        Image(
                            painter = painterResource(id = if (confirmarContrasenaVisible) R.drawable.ojo_abierto else R.drawable.ojo_cerrado),
                            contentDescription = "Mostrar/Ocultar Contraseña"
                        )
                    }
                }
            )

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

            Button(
                onClick = { save() },
                colors = ButtonDefaults.buttonColors(containerColor = backgroundColorButton),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(text = "Registrar", color = Color.White)
            }

            TextButton(onClick = goBack) {
                Text("¿Ya tienes cuenta? Iniciar sesión", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

class PhoneVisualTransformation : VisualTransformation {
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


class PhoneOffsetMapping(private val original: String, private val transformed: String) : OffsetMapping {
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
