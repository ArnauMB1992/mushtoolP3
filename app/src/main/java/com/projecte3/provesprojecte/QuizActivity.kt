package com.projecte3.provesprojecte

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputType
import android.util.Log
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.projecte3.provesprojecte.ui.theme.ProvesProjecte3Theme
import java.text.SimpleDateFormat
import java.util.Date

class QuizActivity : ComponentActivity() {
    private val database = FirebaseDatabase.getInstance() // Crear una instancia de FirebaseDatabase
    private val myRef = database.getReference("scores") // Crear una referencia a la base de datos

    // Lista de preguntas
    private val questions = listOf(
        Question(
            R.drawable.champinon_silvestre,
            "¿Cuál es el nombre de esta seta?",
            listOf("Champiñón silvestre", "Níscalo", "Rusula dorada"),
            "Champiñón silvestre",
            1
        ),
        Question(
            R.drawable.niscalo,
            "¿Cuál es el nombre de esta seta?",
            listOf("Níscalo", "Rusula dorada", "Seta de cardo"),
            "Níscalo",
            1
        ),
        Question(
            R.drawable.rusula_dorada,
            "¿Cuál es el nombre de esta seta?",
            listOf("Rusula dorada", "Seta de cardo", "Champiñón silvestre"),
            "Rusula dorada",
            1
        ),
        Question(
            R.drawable.seta_de_cardo,
            "¿Cuál es el nombre de esta seta?",
            listOf("Seta de cardo", "Champiñón silvestre", "Níscalo"),
            "Seta de cardo",
            1
        ),
        Question(
            R.drawable.collibia_comestible,
            "¿Cuál es el nombre de esta seta?",
            listOf("Collibia comestible", "Higróforo niveo", "Lengua de buey"),
            "Collibia comestible",
            2
        ),
        Question(
            R.drawable.higroforo_niveo,
            "¿Cuál es el nombre de esta seta?",
            listOf("Higróforo niveo", "Lengua de buey", "Boleto livido"),
            "Higróforo niveo",
            2
        ),
        Question(
            R.drawable.lengua_de_buey,
            "¿Cuál es el nombre de esta seta?",
            listOf("Lengua de buey", "Boleto livido", "Collibia comestible"),
            "Lengua de buey",
            2
        ),
        Question(
            R.drawable.boleto_livido,
            "¿Cuál es el nombre de esta seta?",
            listOf("Boleto livido", "Collibia comestible", "Higróforo niveo"),
            "Boleto livido",
            2
        ),
        Question(
            R.drawable.bolet_de_greix,
            "¿Cuál es el nombre de esta seta?",
            listOf("Bolet de greix", "Gita de bruixa", "Collibia agrupada"),
            "Bolet de greix",
            3
        ),
        Question(
            R.drawable.gita_de_bruixa,
            "¿Cuál es el nombre de esta seta?",
            listOf("Gita de bruixa", "Collibia agrupada", "Amanita de los Césares"),
            "Gita de bruixa",
            3
        ),
        Question(
            R.drawable.collibia_agrupada,
            "¿Cuál es el nombre de esta seta?",
            listOf("Collibia agrupada", "Amanita de los Césares", "Seta de la muerte"),
            "Collibia agrupada",
            3
        ),
        Question(
            R.drawable.amanita_de_los_cesares,
            "¿Cuál es el nombre de esta seta?",
            listOf("Amanita de los Césares", "Seta de la muerte", "Bolet de greix"),
            "Amanita de los Césares",
            3
        ),
        Question(
            R.drawable.seta_de_la_muerte,
            "¿Cuál es el nombre de esta seta?",
            listOf("Seta de la muerte", "Bolet de greix", "Collibia agrupada"),
            "Seta de la muerte",
            3
        ),
    )

    // Índice de pregunta actual y puntuación.
    private var currentQuestionIndex = 0 // Índice de la pregunta actual
    private var score = 0 // Puntuación del jugador
    private var timer: CountDownTimer? = null // Temporizador para el tiempo de respuesta

    // Separación de preguntas por dificultad y aleatorización.
    private val easyQuestions = questions.filter { it.dificultad == 1 }.shuffled() // Preguntas fáciles
    private val mediumQuestions = questions.filter { it.dificultad == 2 }.shuffled() // Preguntas de dificultad media
    private val hardQuestions = questions.filter { it.dificultad == 3 }.shuffled() // Preguntas difíciles

    private val orderedQuestions = easyQuestions + mediumQuestions + hardQuestions // Une las listas en el orden correcto

    // Función de creación de la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProvesProjecte3Theme {
                val currentQuestionIndex = remember { mutableStateOf(0) }

                Box(modifier = Modifier.fillMaxSize()) {
                    // Fondo de pantalla con imagen.
                    Image(
                        painter = painterResource(id = R.drawable.mush),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    if (currentQuestionIndex.value < orderedQuestions.size) {
                        val currentQuestion = orderedQuestions[currentQuestionIndex.value]
                        val remainingTime = remember { mutableStateOf(30) }

                        if (timer == null) {
                            startTimer(remainingTime, currentQuestionIndex)
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            // Mostrar la puntuación actual
                            Text(
                                text = "Puntuación: $score",
                                fontSize = 24.sp,
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                color = Color.White // Cambia el color del texto a blanco
                            )
                            // Mostrar el tiempo restante
                            Text(
                                text = "Tiempo: ${remainingTime.value}",
                                fontSize = 24.sp,
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                color = Color.White // Cambia el color del texto a blanco
                            )
                            // Mostrar la dificultad de la pregunta actual
                            Text(
                                text = "Dificultad: ${
                                    when (currentQuestion.dificultad) {
                                        1 -> "Fácil"
                                        2 -> "Media"
                                        3 -> "Difícil"
                                        else -> "Desconocida"
                                    }
                                }",
                                fontSize = 24.sp,
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                color = Color.White // Cambia el color del texto a blanco
                            )
                            // Mostrar la imagen de la pregunta actual
                            Image(
                                painter = painterResource(id = currentQuestion.image),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(200.dp)
                                    .fillMaxWidth()
                                    .border(4.dp, Color.Black),
                                contentScale = ContentScale.Crop
                            )
                            // Mostrar el texto de la pregunta actual
                            Surface(
                                color = Color.White,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(text = currentQuestion.question, fontSize = 24.sp)
                            }
                            // Mostrar las opciones de respuesta y manejar las respuestas del usuario
                            currentQuestion.options.forEach { option ->
                                Button(
                                    onClick = {
                                        if (option == currentQuestion.correctOption) {
                                            // Si la respuesta es correcta, calcular la puntuación
                                            score += remainingTime.value * currentQuestion.dificultad
                                            // Reproducir el sonido correcto
                                            val correctSound = MediaPlayer.create(this@QuizActivity, R.raw.correcto)
                                            correctSound.setOnCompletionListener { it.release() }
                                            correctSound.start()
                                            currentQuestionIndex.value++
                                            if (currentQuestionIndex.value == orderedQuestions.size) {
                                                // Si se han respondido todas las preguntas, reiniciar el juego
                                                currentQuestionIndex.value = 0
                                                checkAndSaveScore(score)
                                                score = 0
                                            }
                                            // Cancelar el temporizador anterior y comenzar uno nuevo
                                            timer?.cancel()
                                            startTimer(remainingTime, currentQuestionIndex)
                                        } else {
                                            // Si la respuesta es incorrecta, reiniciar al inicio
                                            currentQuestionIndex.value = 0
                                            checkAndSaveScore(score)
                                            score = 0
                                            // Reproducir el sonido de fin de juego
                                            val gameOverSound = MediaPlayer.create(this@QuizActivity, R.raw.gameover)
                                            gameOverSound.setOnCompletionListener { it.release() }
                                            gameOverSound.start()
                                            // Cancelar el temporizador anterior y comenzar uno nuevo
                                            timer?.cancel()
                                            startTimer(remainingTime, currentQuestionIndex)
                                        }
                                    }
                                ) {
                                    Text(text = option, fontSize = 24.sp)
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            Spacer(modifier = Modifier.weight(1f)) // Este Spacer llenará el espacio entre las opciones y el botón "Finalizar"

                            // Botón para finalizar el juego
                            Button(
                                onClick = { finish() },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text(
                                    text = "Finalizar",
                                    fontSize = 30.sp
                                ) // Aumenta el tamaño del texto a 30sp
                            }
                        }
                    }
                }
            }
        }
    }

    // Función para iniciar el temporizador
    private fun startTimer(remainingTime: MutableState<Int>, currentQuestionIndex: MutableState<Int>) {
        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime.value = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                Log.d("QuizActivity", "Timer finished")
                // Marcar la pregunta como no acertada
                currentQuestionIndex.value++
                if (currentQuestionIndex.value == questions.size) {
                    // Si todas las preguntas han sido respondidas, reinicia el juego
                    currentQuestionIndex.value = 0
                    checkAndSaveScore(score)
                    score = 0
                } else {
                    // Si la pregunta no ha sido respondida, mostrar un diálogo con la respuesta correcta
                    val currentQuestion = questions[currentQuestionIndex.value]
                    val builder = AlertDialog.Builder(this@QuizActivity)
                        .setTitle("Tiempo agotado")
                        .setMessage("Se acabó el tiempo para esta pregunta. La respuesta correcta era: ${currentQuestion.correctOption}")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                            checkAndSaveScore(score)
                        }
                    Log.d("QuizActivity", "Showing dialog") // Agregar declaración de registro aquí
                    builder.show()
                }
                // Reproducir el sonido de tiempo agotado
                //val timeOverSound = MediaPlayer.create(this@QuizActivity, R.raw.tiempo)
                //timeOverSound.setOnCompletionListener { it.release() }
                //timeOverSound.start()
            }
        }.start()
    }

    // Función para verificar y guardar la puntuación
    private fun checkAndSaveScore(score: Int) {
        Log.d("QuizActivity", "Checking and saving score")

        // Obtener los 10 mejores puntajes de Firebase
        myRef.orderByChild("score").limitToLast(10).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val scores = dataSnapshot.children.mapNotNull { it.child("score").getValue(Int::class.java) }
                if (scores.isEmpty() || score > scores.minOrNull()!!) {
                    // Si el puntaje actual es mayor que el puntaje más bajo en los 10 mejores, guárdelo en Firebase
                    saveScoreToFirebase(score)
                } else {
                    // Si el puntaje no está en los 10 mejores, navegue a la actividad Puntuaciones
                    startActivity(Intent(this@QuizActivity, Puntuaciones::class.java))
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar posibles errores.
            }
        })
    }

    // Función para guardar la puntuación en Firebase
    private fun saveScoreToFirebase(score: Int) {
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDateAndTime: String = sdf.format(Date())

        // Crear un constructor AlertDialog
        val builder = AlertDialog.Builder(this@QuizActivity)
        builder.setTitle("Nombre:")

        // Configurar la entrada
        val input = EditText(this@QuizActivity)
        // Especificar el tipo de entrada esperada
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Configurar los botones
        builder.setPositiveButton("OK") { dialog, _ ->
            val username = input.text.toString()
            val scoreData = mapOf("score" to score, "date" to currentDateAndTime, "user" to username)

            // Guardar el puntaje en Firebase
            myRef.push().setValue(scoreData).addOnCompleteListener {
                // Navegar a la actividad Puntuaciones después de que se haya guardado el puntaje
                startActivity(Intent(this@QuizActivity, Puntuaciones::class.java))
            }

            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }
}

// Definición de la clase Question
data class Question(
    val image: Int,
    val question: String,
    val options: List<String>,
    val correctOption: String,
    val dificultad: Int
)