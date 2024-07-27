package com.sagrd.spellingappv2.ui.componentes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sagrd.spellingappv2.model.Usuario

@Composable
fun RowUsuarios(
    usuario: Usuario,
    onClick: (Usuario) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clickable { onClick(usuario) }
            .size(width = 30.dp, height = 30.dp),
        )
    {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = usuario.nombres,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Cursive
            )
            Text(
                text = usuario.edad.toString() + " " + "años",
                fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.End,
                color = Color.Black
            )
        }
    }
}