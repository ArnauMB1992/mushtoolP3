package com.projecte3.provesprojecte

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.projecte3.provesprojecte.ui.theme.ProvesProjecte3Theme

class Puntuaciones : ComponentActivity() { // Define la clase Puntuaciones que extiende de ComponentActivity
    private lateinit var database: DatabaseReference // Declara una referencia a la base de datos de Firebase

    override fun onCreate(savedInstanceState: Bundle?) { // Sobrescribe el método onCreate de la actividad
        super.onCreate(savedInstanceState) // Llama al método onCreate de la clase base
        database = FirebaseDatabase.getInstance().getReference("scores") // Obtiene una referencia a la ubicación "scores" en la base de datos

        setContent { // Establece el contenido de la actividad
            ProvesProjecte3Theme { // Aplica el tema personalizado de la aplicación
                var scores by remember { mutableStateOf(listOf<Triple<Int, String, String>>()) } // Define un estado para almacenar las puntuaciones

                val backgroundImage = painterResource(id = R.drawable.mush) // Obtiene el recurso de imagen de fondo

                Box( // Crea un contenedor Box
                    modifier = Modifier.fillMaxSize(), // Establece el tamaño máximo del contenedor
                    contentAlignment = Alignment.Center // Alinea el contenido al centro del contenedor
                ) {
                    Image( // Muestra una imagen
                        painter = backgroundImage, // Establece el pintor de la imagen
                        contentDescription = null, // Descripción de contenido nula
                        modifier = Modifier.fillMaxSize(), // Establece el tamaño máximo de la imagen
                        contentScale = ContentScale.FillBounds // Escala el contenido para que llene completamente el área del contenedor
                    )
                    Column(modifier = Modifier.padding(16.dp)) { // Crea una columna con un relleno de 16dp
                        Image( // Muestra una imagen
                            painter = painterResource(id = R.drawable.setapp), // Establece el pintor de la imagen
                            contentDescription = null, // Descripción de contenido nula
                            modifier = Modifier.align(Alignment.CenterHorizontally) // Alinea la imagen al centro horizontalmente
                        )
                        Row( // Crea una fila
                            modifier = Modifier.fillMaxWidth(), // Establece el ancho máximo de la fila
                            horizontalArrangement = Arrangement.SpaceBetween // Distribuye los elementos horizontalmente con espacio entre ellos
                        ) {
                            Text(text = "Nombre", modifier = Modifier.weight(1f), color = Color.White) // Muestra un texto para el nombre con peso 1
                            Text(text = "Puntuación", modifier = Modifier.weight(1f), color = Color.White) // Muestra un texto para la puntuación con peso 1
                            Text(text = "Fecha", modifier = Modifier.weight(1f), color = Color.White) // Muestra un texto para la fecha con peso 1
                        }
                        scores.forEach { (score, date, user) -> // Itera sobre las puntuaciones
                            Spacer(modifier = Modifier.height(8.dp)) // Agrega un espacio vertical de 8dp
                            Row( // Crea una fila
                                modifier = Modifier.fillMaxWidth(), // Establece el ancho máximo de la fila
                                horizontalArrangement = Arrangement.SpaceBetween // Distribuye los elementos horizontalmente con espacio entre ellos
                            ) {
                                Text(text = user, modifier = Modifier.weight(1f), color = Color.White) // Muestra el nombre de usuario con peso 1
                                Text(text = score.toString(), modifier = Modifier.weight(1f), color = Color.White) // Muestra la puntuación con peso 1
                                Text(text = date, modifier = Modifier.weight(1f), color = Color.White) // Muestra la fecha con peso 1
                            }
                        }
                    }

                    Box( // Crea otro contenedor Box
                        modifier = Modifier.fillMaxSize(), // Establece el tamaño máximo del contenedor
                        contentAlignment = Alignment.BottomCenter // Alinea el contenido en la parte inferior y centro del contenedor
                    ) {
                        // Botón para volver a GameActivity
                        Button(onClick = {
                            startActivity(Intent(this@Puntuaciones, GameActivity::class.java)) // Inicia GameActivity al hacer clic en el botón
                        }) {
                            Text(text = "Volver") // Muestra el texto "Volver" en el botón
                        }
                    }
                }

                database.addValueEventListener(object : ValueEventListener { // Agrega un listener para cambios en los datos de Firebase
                    override fun onDataChange(dataSnapshot: DataSnapshot) { // Método invocado cuando los datos cambian
                        val allScores = dataSnapshot.children.mapNotNull { snapshot -> // Mapea los datos de la instantánea
                            val score = snapshot.child("score").getValue(Int::class.java) // Obtiene la puntuación
                            val date = snapshot.child("date").getValue(String::class.java) // Obtiene la fecha
                            val user = snapshot.child("user").getValue(String::class.java) // Obtiene el nombre de usuario
                            if (score != null && date != null && user != null) { // Verifica que los datos no sean nulos
                                Triple(score, date, user) // Devuelve una tripleta con los datos
                            } else {
                                null // Devuelve nulo si algún dato es nulo
                            }
                        }
                        // Ordena las puntuaciones en orden descendente y toma las 10 más altas
                        scores = allScores.sortedByDescending { it.first }.take(10) // Actualiza el estado de las puntuaciones
                    }

                    override fun onCancelled(databaseError: DatabaseError) { // Método invocado cuando se cancela la operación
                        // Maneja posibles errores
                        println("Error: ${databaseError.message}") // Imprime el mensaje de error
                    }
                })
            }
        }

    }
}