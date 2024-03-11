package com.projecte3.provesprojecte

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
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

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.mush),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds // Añadido esta línea
            )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.setapp),
                contentDescription = "Setapp Logo",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(text = "Registro", style = MaterialTheme.typography.h4, color = Color.White)

            Spacer(modifier = Modifier.height(16.dp))

// Email TextField
            TextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text("Email", color = Color.White) },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    cursorColor = Color.White,
                    leadingIconColor = Color.White,
                    trailingIconColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                    disabledIndicatorColor = Color.White,
                    errorIndicatorColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    disabledLabelColor = Color.White,
                    errorLabelColor = Color.White,
                    placeholderColor = Color.White,
                    backgroundColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

// Password TextField
            TextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text("Contraseña", color = Color.White) },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    cursorColor = Color.White,
                    leadingIconColor = Color.White,
                    trailingIconColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                    disabledIndicatorColor = Color.White,
                    errorIndicatorColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    disabledLabelColor = Color.White,
                    errorLabelColor = Color.White,
                    placeholderColor = Color.White,
                    backgroundColor = Color.Transparent
                )
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
