package com.projecte3.provesprojecte

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.projecte3.provesprojecte.ui.theme.ProvesProjecte3Theme

class Puntuaciones : ComponentActivity() {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().getReference("scores")

        setContent {
            ProvesProjecte3Theme {
                var scores by remember { mutableStateOf(listOf<Triple<Int, String, String>>()) }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "User", modifier = Modifier.weight(1f))
                            Text(text = "Score", modifier = Modifier.weight(1f))
                            Text(text = "Date", modifier = Modifier.weight(1f))
                        }
                        scores.forEach { (score, date, user) ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = user, modifier = Modifier.weight(1f))
                                Text(text = score.toString(), modifier = Modifier.weight(1f))
                                Text(text = date, modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        // boton para ir a GameActivity
                        Button(onClick = {
                            startActivity(Intent(this@Puntuaciones, GameActivity::class.java))
                        }) {
                            Text(text = "Volver")
                        }
                    }
                }

                database.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val allScores = dataSnapshot.children.mapNotNull { snapshot ->
                            val score = snapshot.child("score").getValue(Int::class.java)
                            val date = snapshot.child("date").getValue(String::class.java)
                            val user = snapshot.child("user").getValue(String::class.java)
                            if (score != null && date != null && user != null) {
                                Triple(score, date, user)
                            } else {
                                null
                            }
                        }
                        // Ordenar las puntuaciones en orden descendente y tomar las 10 más altas
                        scores = allScores.sortedByDescending { it.first }.take(10)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle possible errors.
                        println("Error: ${databaseError.message}")
                    }
                })
            }
        }

    }
}