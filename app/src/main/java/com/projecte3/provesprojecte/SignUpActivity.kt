package com.projecte3.provesprojecte

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.projecte3.provesprojecte.ui.theme.ProvesProjecte3Theme

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        setContent {
            ProvesProjecte3Theme {
                // Set up the UI using Composable functions
                SignUpScreen()
            }
        }
    }

    @Composable
    fun SignUpScreen() {
        val emailState = remember { mutableStateOf(TextFieldValue()) }
        val passwordState = remember { mutableStateOf(TextFieldValue()) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Registro", style = MaterialTheme.typography.h4)

            Spacer(modifier = Modifier.height(16.dp))

            // Email TextField
            TextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text("Email") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Password TextField
            TextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text("Contraseña") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up Button
            val context = LocalContext.current
            Button(
                onClick = {
                    signUpUser(emailState.value.text, passwordState.value.text, context)
                }
            ) {
                Text("Registrarse")
            }
        }
    }

    private fun signUpUser(email: String, password: String, context: android.content.Context) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Registro exitoso.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(context, "Registro fallido.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
