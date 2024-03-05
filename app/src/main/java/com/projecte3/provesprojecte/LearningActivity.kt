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

class LearningActivity  : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProvesProjecte3Theme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.mush),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                            painter = painterResource(id = R.drawable.setapp),
                            contentDescription = null
                            )
                            Row {
                                Button(onClick = {
                                     val intent = Intent(this@LearningActivity, WikiSetas::class.java)
                                    startActivity(intent)
                                }) {
                                    Text(text = "WIKISETA", fontSize = 24.sp)
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Button(onClick = {
                                    val intent = Intent(this@LearningActivity, GameActivity::class.java)
                                    startActivity(intent)
                                }) {
                                    Text(text = "GAME", fontSize = 24.sp)
                                }
                            }
                        }

                        Button(
                            onClick = {
                                val intent = Intent(this@LearningActivity, MainActivity::class.java)
                                startActivity(intent)
                            },
                            modifier = Modifier.align(Alignment.BottomCenter)
                        ) {
                            Text(text = "volver", fontSize = 24.sp)
                        }
                    }
                }
            }
        }
    }
}