package com.sagrd.spellingappv2.ui.componentes

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ValidationText (estado : Boolean){
    val assistiveElementText = if(estado) "Error: Obligatorio" else "*Obligatorio"
    val assitiveElementColor = if(estado){
        MaterialTheme.colors.error
    }else{
        MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)
    }

    Text(
        text = assistiveElementText,
        color = assitiveElementColor,
        style = MaterialTheme.typography.caption,
        modifier = Modifier.padding(start = 10.dp)
    )
}