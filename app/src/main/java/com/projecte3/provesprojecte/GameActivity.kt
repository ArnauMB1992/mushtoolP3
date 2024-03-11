package com.projecte3.provesprojecte

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projecte3.provesprojecte.ui.theme.ProvesProjecte3Theme

class GameActivity : ComponentActivity() { // Define la clase GameActivity que extiende de ComponentActivity
    override fun onCreate(savedInstanceState: Bundle?) { // Sobrescribe el método onCreate de la actividad
        super.onCreate(savedInstanceState) // Llama al método onCreate de la clase base
        setContent { // Establece el contenido de la actividad
            ProvesProjecte3Theme { // Aplica el tema personalizado de la aplicación
                Surface( // Crea una superficie para colocar contenido encima
                    modifier = Modifier.fillMaxSize(), // Establece el tamaño máximo de la superficie
                    color = MaterialTheme.colorScheme.background // Establece el color de fondo de la superficie
                ) {
                    Box(modifier = Modifier.fillMaxSize()) { // Crea un contenedor Box que ocupa toda la pantalla
                        Image( // Muestra una imagen
                            painter = painterResource(id = R.drawable.mush), // Establece el pintor de la imagen
                            contentDescription = null, // Descripción de contenido nula
                            modifier = Modifier.fillMaxSize(), // Establece el tamaño máximo de la imagen
                            contentScale = ContentScale.Crop // Escala y recorta la imagen para que llene completamente el área del contenedor
                        )
                        Column( // Crea una columna vertical
                            modifier = Modifier.align(Alignment.Center), // Alinea la columna al centro verticalmente
                            horizontalAlignment = Alignment.CenterHorizontally // Alinea los elementos de la columna al centro horizontalmente
                        ) {
                            Image( // Muestra una imagen
                                painter = painterResource(id = R.drawable.setapp), // Establece el pintor de la imagen
                                contentDescription = null // Descripción de contenido nula
                            )
                            Row { // Crea una fila horizontal
                                Button(onClick = { // Crea un botón y establece el comportamiento al hacer clic
                                    val intent = Intent(this@GameActivity, Puntuaciones::class.java) // Crea una intención para abrir la actividad de puntuaciones
                                    startActivity(intent) // Inicia la actividad de puntuaciones
                                }) {
                                    Text(text = "PUNTUACIONES", fontSize = 24.sp) // Muestra el texto "PUNTUACIONES" en el botón con tamaño de fuente 24sp
                                }
                                Spacer(modifier = Modifier.width(16.dp)) // Agrega un espacio horizontal de 16dp entre los botones
                                Button(onClick = { // Crea un botón y establece el comportamiento al hacer clic
                                    val intent = Intent(this@GameActivity, QuizActivity::class.java) // Crea una intención para abrir la actividad de juego
                                    startActivity(intent) // Inicia la actividad de juego
                                }) {
                                    Text(text = "GAME", fontSize = 24.sp) // Muestra el texto "GAME" en el botón con tamaño de fuente 24sp
                                }
                            }
                        }
                        Box( // Crea otro contenedor Box
                            modifier = Modifier.fillMaxSize(), // Establece el tamaño máximo del contenedor
                            contentAlignment = Alignment.BottomCenter // Alinea el contenido en la parte inferior y centro del contenedor
                        ) {
                            Button(onClick = { // Crea un botón y establece el comportamiento al hacer clic
                                val intent = Intent(this@GameActivity, LearningActivity::class.java) // Crea una intención para abrir la actividad de aprendizaje
                                startActivity(intent) // Inicia la actividad de aprendizaje
                            }) {
                                Text(text = "Volver") // Muestra el texto "Volver" en el botón
                            }
                        }
                    }
                }
            }
        }
    }
}