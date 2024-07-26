package com.sagrd.spellingappv2.ui.componentes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.sagrd.spellingappv2.model.Palabra

@Composable
fun RowPalabra(
    palabra: Palabra,
    onClick: (Palabra) -> Unit
) {
    /*Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Word: ${palabra.palabra}")
        Text(text = "Description: ${palabra.descripcion}")
        Text(text = "Image Url: ${palabra.imagenUrl}")
    }*/
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clickable { onClick(palabra) }
           .size(width = 30.dp, height = 80.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(4.dp),

        )
    {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(palabra.imagenUrl)
                    .transformations(CircleCropTransformation())
                    .build(),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {

                Text(
                    text = palabra.palabra,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = palabra.descripcion,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.End,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis
                )


            }

        }
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.Start
//        ) {
//
//
//        }

    }
}