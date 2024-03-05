package com.projecte3.provesprojecte

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MadeFor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MadeForContent()
        }
    }
}

@Composable
fun MadeForContent() {
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val image: Painter = painterResource(id = R.drawable.mush)
        Image(
            painter = image,
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize().aspectRatio(image.intrinsicSize.width / image.intrinsicSize.height, matchHeightConstraintsFirst = true),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Primer bloque: Textos
            Column {
                Text(
                    text = "Créditos de la Aplicación \"SETAPP Mushroom\"",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Segundo bloque: Botones alineados horizontalmente
            Row(horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "Desarrollado por: ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(10.dp)) // Agrega un salto de línea
            Row(horizontalArrangement = Arrangement.Center) {
                Button(onClick = {
                    val url = "https://github.com/Alexyz17"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    context.startActivity(intent)
                }) {
                    Text("Alex", color = Color.White)
                }
                Button(onClick = {
                    val url = "https://github.com/ArnauMB1992"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    context.startActivity(intent)
                }) {
                    Text("Arnau", color = Color.White)
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = 32.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tercer bloque: Textos
            Column {
                Text(
                    text = "Agradecimientos Especiales:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                Text(
                    text = "ChapGpt y Copilot",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                Text(
                    text = "Copyright © Año de Publicación 2024\nTodos los derechos reservados.",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                Button(onClick = {
                    context.startActivity(Intent(context, MushroomActivity::class.java))
                }) {
                    androidx.compose.material3.Text(text = "Volver")
                }
            }
        }
    }
}