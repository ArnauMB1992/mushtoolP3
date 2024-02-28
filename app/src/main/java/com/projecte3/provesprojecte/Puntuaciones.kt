package com.projecte3.provesprojecte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.database.*
import com.projecte3.provesprojecte.ui.theme.ProvesProjecte3Theme

class Puntuaciones : ComponentActivity() {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().getReference("scores")

        setContent {
            ProvesProjecte3Theme {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    database.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val scores = dataSnapshot.children.map { snapshot ->
                                val score = snapshot.child("score").getValue(Int::class.java)
                                val date = snapshot.child("date").getValue(String::class.java)
                                val user = snapshot.child("user").getValue(String::class.java)
                                "User: $user, Score: $score, Date: $date"
                            }
                            items(scores) { score ->
                                Text(text = score)
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle possible errors.
                        }
                    })
                }
            }
        }
    }
}