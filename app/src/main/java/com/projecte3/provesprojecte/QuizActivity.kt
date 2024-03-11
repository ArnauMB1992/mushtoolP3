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

    private var currentQuestionIndex = 0
    private var score = 0
    private var timer: CountDownTimer? = null

    private val easyQuestions = questions.filter { it.dificultad == 1 }.shuffled()
    private val mediumQuestions = questions.filter { it.dificultad == 2 }.shuffled()
    private val hardQuestions = questions.filter { it.dificultad == 3 }.shuffled()

    private val orderedQuestions = easyQuestions + mediumQuestions + hardQuestions // Une las listas en el orden correcto

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
                            Text(
                                text = "Puntuación: $score",
                                fontSize = 24.sp,
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                color = Color.White // Cambia el color del texto a blanco
                            )
                            Text(
                                text = "Tiempo: ${remainingTime.value}",
                                fontSize = 24.sp,
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                color = Color.White // Cambia el color del texto a blanco
                            )
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
                                        if (option == currentQuestion.correctOption) {
                                            // If the answer is correct, calculate the score based on remaining time and question difficulty
                                            score += remainingTime.value * currentQuestion.dificultad
                                            // Play the correct sound
                                            val correctSound = MediaPlayer.create(this@QuizActivity, R.raw.correcto)
                                            correctSound.start()
                                            currentQuestionIndex.value++
                                            if (currentQuestionIndex.value == orderedQuestions.size) {
                                                // If all questions have been answered, restart the game
                                                currentQuestionIndex.value = 0
                                                checkAndSaveScore(score)
                                                score = 0
                                            }
                                            // Cancel the previous timer and start a new one
                                            timer?.cancel()
                                            startTimer(remainingTime, currentQuestionIndex)
                                        } else {
                                            // If the answer is incorrect, go back to the first question
                                            currentQuestionIndex.value = 0
                                            checkAndSaveScore(score)
                                            score = 0
                                            // Play the GameOver sound
                                            val gameOverSound = MediaPlayer.create(this@QuizActivity, R.raw.gameover)
                                            gameOverSound.start()
                                            // Cancel the previous timer and start a new one
                                            timer?.cancel()
                                            //startTimer(remainingTime, currentQuestionIndex)
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
                    // Si la pregunta no ha sido respondida, continúa con la siguiente
                    val currentQuestion = questions[currentQuestionIndex.value]
                    if (!isFinishing) {
                        val builder = AlertDialog.Builder(this@QuizActivity)
                            .setTitle("Tiempo agotado")
                            .setMessage("Se acabó el tiempo para esta pregunta. La respuesta correcta era: ${currentQuestion.correctOption}")
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                            }
                        startActivity(Intent(this@QuizActivity, Puntuaciones::class.java))
                        builder.show()
                    }
                }
                // Play the TimeOver sound
                val timeOverSound = MediaPlayer.create(this@QuizActivity, R.raw.tiempo)
                timeOverSound.start()
            }
        }.start()
    }

    private fun checkAndSaveScore(score: Int) {
        Log.d("QuizActivity", "Checking and saving score")

        // Retrieve the top 10 scores from Firebase
        myRef.orderByChild("score").limitToLast(10).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val scores = dataSnapshot.children.mapNotNull { it.child("score").getValue(Int::class.java) }
                if (scores.isEmpty() || score > scores.minOrNull()!!) {
                    // If the current score is higher than the lowest score in the top 10, save it to Firebase
                    saveScoreToFirebase(score)
                } else {
                    // If the score is not in the top 10, navigate to Puntuaciones activity
                    startActivity(Intent(this@QuizActivity, Puntuaciones::class.java))
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors.
            }
        })
    }

    private fun saveScoreToFirebase(score: Int) {
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDateAndTime: String = sdf.format(Date())

        // Create an AlertDialog builder
        val builder = AlertDialog.Builder(this@QuizActivity)
        builder.setTitle("Nombre:")

        // Set up the input
        val input = EditText(this@QuizActivity)
        // Specify the type of input expected
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK") { dialog, _ ->
            val username = input.text.toString()
            val scoreData = mapOf("score" to score, "date" to currentDateAndTime, "user" to username)

            // Save the score to Firebase
            myRef.push().setValue(scoreData).addOnCompleteListener {
                // Navigate to Puntuaciones activity after the score has been saved
                startActivity(Intent(this@QuizActivity, Puntuaciones::class.java))
            }

            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }
}

data class Question(
    val image: Int,
    val question: String,
    val options: List<String>,
    val correctOption: String,
    val dificultad: Int
)