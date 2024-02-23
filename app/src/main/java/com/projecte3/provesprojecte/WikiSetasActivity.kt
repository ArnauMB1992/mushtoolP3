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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projecte3.provesprojecte.com.projecte3.provesprojecte.SetaDetailActivity
import java.text.SimpleDateFormat

class WikiSetasActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mma")

        setContent {
            val setas = listOf(
                Seta(
                    "Agaricus xanthodermus",
                    null.toString(), 0.0, 0.0, sdf.parse("1/1/1990 1:00PM"), R.drawable.agaricusxanthodermus
                ),
                Seta(
                    "Agaricus campestris",
                    null.toString(), 0.0, 0.0, sdf.parse("1/1/1990 1:00PM"), R.drawable.agaricuscampestris
                ),
                Seta(
                    "Amanita Muscaria",
                    null.toString(), 0.0, 0.0, sdf.parse("1/1/1990 1:00PM"), R.drawable.amanitamuscaria
                ),
                Seta(
                    "Amanita Phalloides",
                    null.toString(), 0.0, 0.0, sdf.parse("1/1/1990 1:00PM"), R.drawable.amanitaphalloides
                ),
                Seta(
                    "Calocybe gambosa",
                    null.toString(), 0.0, 0.0, sdf.parse("1/1/1990 1:00PM"), R.drawable.calocybegambosa
                ),
                Seta(
                    "Craterellus cornucopioides",
                    null.toString(), 0.0, 0.0, sdf.parse("1/1/1990 1:00PM"), R.drawable.craterelluscornucopioides
                ),
                Seta(
                    "Infundibulicybe geotropa",
                    null.toString(), 0.0, 0.0, sdf.parse("1/1/1990 1:00PM"), R.drawable.infundibulicybegeotropa
                ),
                Seta(
                    "Leccinellumg riseum",
                    null.toString(), 0.0, 0.0, sdf.parse("1/1/1990 1:00PM"), R.drawable.leccinellumgriseum
                ),
            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column {
                    LazyColumn {
                        items(setas) { seta ->
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable {
                                        val intent = Intent(this@WikiSetasActivity, SetaDetailActivity::class.java).apply {
                                            putExtra("name", seta.nombre)
                                            putExtra("imageRes", seta.imageRes)
                                        }
                                        startActivity(intent)
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = seta.imageRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(84.dp)
                                )
                                Text(
                                    text = seta.nombre,
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
                    Text(text = "Volver", fontSize = 20.sp) // Aumenta el tamaño del texto a 30sp
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