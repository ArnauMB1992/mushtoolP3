package com.projecte3.provesprojecte

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.threetenabp.AndroidThreeTen
import com.projecte3.provesprojecte.ui.theme.ProvesProjecte3Theme

@Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        AndroidThreeTen.init(this)
        setContent {
            ProvesProjecte3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
        requestAllPermissions()
    }

    private fun requestAllPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        val allPermissionsGranted = permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

        if (!allPermissionsGranted) {
            ActivityCompat.requestPermissions(this, permissions, 0)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            val allPermissionsGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (allPermissionsGranted) {
                Toast.makeText(this, "Todos los permisos concedidos", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No se concedieron todos los permisos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val user = FirebaseAuth.getInstance().currentUser
    NavHost(navController, startDestination = if (user == null) "login_screen" else "main_screen") {
        composable("login_screen") { LoginScreen(navController) }
        composable("main_screen") { MainMenu() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
    val auth = FirebaseAuth.getInstance()

    TopAppBar(
        title = { Text("MushTool") },
        actions = {
            IconButton(onClick = {
                // Handle settings click here
            }) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings")
            }
            IconButton(onClick = {
                // Handle logout click here
                auth.signOut()
                navController.navigate("login_screen")
            }) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
            }
        }
    )
}

@Composable
fun LoginScreen(navController: NavController) {
    val emailState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Inicio de sesión",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email TextField
        TextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation() // Oculta el texto de la contraseña
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        Button(
            onClick = {
                loginUser(emailState.value.text, passwordState.value.text, navController)
            }
        ) {
            Text("Iniciar sesión")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign Up Button
        val context = LocalContext.current // Obtén el Context actual
        Button(
            onClick = {
                // Crea un Intent para iniciar SignUpActivity
                val intent = Intent(context, SignUpActivity::class.java)
                // Usa el Context para llamar a startActivity()
                context.startActivity(intent)
            }
        ) {
            Text("Registrarse")
        }
    }
}

private fun loginUser(email: String, password: String, navController: NavController) {
    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Authentication successful, navigate to main screen
                navController.navigate("main_screen")
            } else {
                // Authentication failed
                Toast.makeText(navController.context, "Inicio de sesión fallido.", Toast.LENGTH_SHORT).show()
            }
        }
}

@Preview(showBackground = true)
@Composable
fun MainMenu() {
    Column {
        val navController = rememberNavController()
        TopBar(navController)
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.mush),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Greeting("User")
        }
    }
}

@Composable
fun Greeting(name: String , modifier: Modifier = Modifier) {
    var calGo by remember {mutableStateOf(0)}
    val context = LocalContext.current

    when (calGo) {
        1 -> {
            context.startActivity(Intent(context, CameraActivity::class.java))
        }
        2 -> {
            context.startActivity(Intent(context, MushroomActivity::class.java))
        }
        3 -> {
            context.startActivity(Intent(context, WeatherActivity::class.java))
        }
        4 -> {
            context.startActivity(Intent(context, LearningActivity::class.java))
        }
        5 -> {
            context.startActivity(Intent(context, MadeFor::class.java))
        }
    }

    //
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Image(
                painter = painterResource(id = R.drawable.setapp),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Hello!",
                fontSize = 40.sp, // Increase font size
                modifier = modifier,
                color = Color.White // Cambia el color del texto a blanco
            )
            Row {
                Button(onClick = {calGo = 1}, modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    Text(text = "BUSCAR", fontSize = 30.sp)
                }
                Button(onClick = {calGo = 2}, modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    Text(text = "MIS SETAS", fontSize = 30.sp)
                }
            }
            Row {
                Button(onClick = {calGo = 3}, modifier = Modifier.padding(bottom = 10.dp)) {
                    Text(text = "TIEMPO", fontSize = 30.sp)
                }
                Button(onClick = {calGo = 4}, modifier = Modifier.padding(bottom = 10.dp)) {
                    Text(text = "APRENDER", fontSize = 30.sp)
                }
            }
            Button(onClick = {calGo = 5}) {
                Text(text = "MushTools", fontSize = 30.sp)
            }
        }
    }
}