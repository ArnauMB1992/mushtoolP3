package com.projecte3.provesprojecte

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.projecte3.provesprojecte.ui.theme.ProvesProjecte3Theme

@Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProvesProjecte3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainMenu()
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
fun TopBar() {
    TopAppBar(
        title = { Text("MushTool") },
        actions = {
            IconButton(onClick = { /* Handle settings click here */ }) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MainMenu() {
    Column {
        TopBar()
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
        7 -> {
            context.startActivity(Intent(context, BackActivity::class.java))
        }
    }

    //
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Text(
                text = "Hello $name!",
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
            Button(onClick = {calGo = 5}, modifier = Modifier.padding(bottom = 10.dp)) {
                Text(text = "Mensajes", fontSize = 30.sp)
            }

            Button(onClick = {calGo = 6}) {
                Text(text = "MushPhotos", fontSize = 30.sp)
            }
        }
    }
}