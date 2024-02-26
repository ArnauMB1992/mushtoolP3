package com.projecte3.provesprojecte

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.projecte3.provesprojecte.com.projecte3.provesprojecte.SetaDetailActivity

private const val TAG = "WikiSetasActivity"

class WikiSetasActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val db = FirebaseFirestore.getInstance()
            val listaSetas = remember { mutableStateOf(listOf<Setas>()) }

            db.collection("setas")
                .get()
                .addOnSuccessListener { result ->
                    listaSetas.value = result.map { document ->
                        Setas(
                            document.getString("nombre_comun") ?: "",
                            document.getString("nombre_cientifico") ?: "",
                            document.getString("familia") ?: "",
                            document.getString("comestible") ?: "",
                            document.getString("sombrero") ?: "",
                            document.getString("pie") ?: "",
                            document.getString("carne") ?: "",
                            document.getString("habitat") ?: "",
                            document.getString("observaciones") ?: "",
                            document.getString("foto") ?: "",
                            document.getString("comun") ?: "",
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column {
                    LazyColumn {
                        items(listaSetas.value) { setas ->
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable {
                                        val intent = Intent(this@WikiSetasActivity, SetaDetailActivity::class.java).apply {
                                            putExtra("name", setas.nombre_comun)
                                            putExtra("imageRes", setas.foto)
                                        }
                                        startActivity(intent)
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = setas.foto.toInt()),
                                    contentDescription = null,
                                    modifier = Modifier.size(84.dp)
                                )
                                Text(
                                    text = setas.nombre_comun,
                                    color = Color.Black,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                }
                Button(
                    onClick = { finish() },
                ) {
                    Text(text = "Volver", fontSize = 20.sp)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("SetaDetailActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("WikiSetasActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("WikiSetasActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("WikiSetasActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("WikiSetasActivity", "onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("WikiSetasActivity", "onRestart")
        // Re-initialize your resources here
    }
}