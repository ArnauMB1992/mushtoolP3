package com.projecte3.provesprojecte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.database.*
import com.projecte3.provesprojecte.ui.theme.ProvesProjecte3Theme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

class Puntuaciones : ComponentActivity() {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().getReference("scores")

        setContent {
            ProvesProjecte3Theme {
                var scores by remember { mutableStateOf(listOf<Triple<Int, String, String>>()) }

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