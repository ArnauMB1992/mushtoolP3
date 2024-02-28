package com.projecte3.provesprojecte

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.FirebaseDatabase
import com.projecte3.provesprojecte.ui.theme.ProvesProjecte3Theme
import java.text.SimpleDateFormat
import java.util.Date

class QuizActivity : ComponentActivity() {
    // Crear una instancia de FirebaseDatabase
    private val database = FirebaseDatabase.getInstance()
    // Crear una referencia a la base de datos
    private val myRef = database.getReference("scores")

    // Lista de preguntas
    private val questions = listOf(
        Question(R.drawable.agaricusxanthodermus,"¿Cuál es el nombre de esta seta?", listOf("Agaricus_Xanthodermus", "Agaricus_campestris", "Amanita_phalloides"), "Agaricus_Xanthodermus"),
        Question(R.drawable.agaricuscampestris,"¿Cuál es el nombre de esta seta?", listOf("Agaricus_campestris", "Amanita_phalloides", "Infundibulicybe_geotropa"), "Agaricus_campestris"),
        Question(R.drawable.amanitaphalloides,"¿Cuál es el nombre de esta seta?", listOf("Amanita_phalloides", "Infundibulicybe_geotropa", "Amanita_muscaria"), "Amanita_phalloides"),
        Question(R.drawable.infundibulicybegeotropa,"¿Cuál es el nombre de esta seta?", listOf("Infundibulicybe_geotropa", "Amanita_muscaria", "Calocybe_gambosa"), "Infundibulicybe_geotropa"),
        Question(R.drawable.amanitamuscaria,"¿Cuál es el nombre de esta seta?", listOf("Amanita_muscaria", "Calocybe_gambosa", "Craterellus_cornucopioides"), "Amanita_muscaria"),
        Question(R.drawable.calocybegambosa,"¿Cuál es el nombre de esta seta?", listOf("Calocybe_gambosa", "Craterellus_cornucopioides", "Leccinellum_griseum"), "Calocybe_gambosa"),
        Question(R.drawable.craterelluscornucopioides,"¿Cuál es el nombre de esta seta?", listOf("Craterellus_cornucopioides", "Leccinellum_griseum", "Psilocybe_cubensis"), "Craterellus_cornucopioides"),
        Question(R.drawable.leccinellumgriseum,"¿Cuál es el nombre de esta seta?", listOf("Leccinellum_griseum", "Agaricus_campestris", "Agaricus_bisporus"), "Leccinellum_griseum"),
    )
    private var currentQuestionIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProvesProjecte3Theme {

                val currentQuestionIndex = remember { mutableStateOf(0) }

                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.mush),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    if (currentQuestionIndex.value < questions.size) {
                        val currentQuestion = questions[currentQuestionIndex.value]
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Image(
                                painter = painterResource(id = currentQuestion.image),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(200.dp)
                                    .fillMaxWidth()
                                    .border(4.dp, Color.Black),
                                contentScale = ContentScale.Crop
                            )
                            Surface(
                                color = Color.White,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(text = currentQuestion.question, fontSize = 24.sp)
                            }
                            currentQuestion.options.forEach { option ->
                                Button(
                                    onClick = {
                                        if (option == currentQuestion.correctOption){
                                        // Si la respuesta es correcta, pasa a la siguiente pregunta
                                            score += 10
                                            currentQuestionIndex.value++
                                            if (currentQuestionIndex.value == questions.size) {
                                                // Si todas las preguntas han sido respondidas, reinicia el juego
                                                Toast.makeText(this@QuizActivity, "Puntuación: $score", Toast.LENGTH_LONG).show() // Display the score
                                                currentQuestionIndex.value = 0
                                                score = 0

                                            }
                                            // Guardar la puntuación en Firebase
                                            saveScoreToFirebase(score)
                                        } else {
                                            // Si la respuesta es incorrecta, vuelve a la primera pregunta
                                            Toast.makeText(this@QuizActivity, "Puntuación: $score", Toast.LENGTH_LONG).show() // Display the score
                                            currentQuestionIndex.value = 0
                                            score = 0

                                        }
                                    }

                                ) {
                                    Text(text = option, fontSize = 24.sp)
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            Spacer(modifier = Modifier.weight(1f)) // Este Spacer llenará el espacio entre las opciones y el botón "Finalizar"

                            Button(
                                onClick = { finish() },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text(text = "Finalizar", fontSize = 30.sp) // Aumenta el tamaño del texto a 30sp
                            }
                        }
                    }
                }
            }
        }
    }
    private fun saveScoreToFirebase(score: Int) {
        val sdf = SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z")
        val currentDateAndTime: String = sdf.format(Date())
        val scoreData = mapOf("score" to score, "date" to currentDateAndTime, "user" to "ArnauM" )
        myRef.push().setValue(scoreData)
    }
}

data class Question(
    val image: Int,
    val question: String,
    val options: List<String>,
    val correctOption: String
)