package com.sagrd.spellingappv2.ui.componentes

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ValidationText (estado : Boolean){
    val assistiveElementText = if(estado) "Error: Obligatorio" else "*Obligatorio"
    val assitiveElementColor = if(estado){
        MaterialTheme.colorScheme.error
    }else{
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
    }

    Text(
        text = assistiveElementText,
        color = assitiveElementColor,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(start = 10.dp)
    )
}