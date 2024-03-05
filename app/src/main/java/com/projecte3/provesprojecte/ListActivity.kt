package com.projecte3.provesprojecte

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            SetaManager.loadSetas()
            val mushrooms = SetaManager.setas
            withContext(Dispatchers.Main) {
                setContent {
                    MushroomListScreen(mushrooms, this@ListActivity, lifecycleScope)
                }
            }
        }
    }
}

fun decodeBase64ToBitmap(base64String: String): Bitmap {
    val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

@Composable
fun MushroomListScreen(mushrooms: List<Seta>, context: Context, lifecycleScope: LifecycleCoroutineScope) {
    var mushrooms by remember { mutableStateOf(mushrooms) } // Initialize with the passed parameter
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showOptionsDialog by remember { mutableStateOf(false) }
    var selectedMushroom by remember { mutableStateOf<Seta?>(null) }

    // Función para recargar las setas
    val reloadMushrooms = {
        lifecycleScope.launch {
            val newMushrooms = SetaManager.loadSetas()
            mushrooms = newMushrooms
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn {
            items(mushrooms) { mushroom ->
                val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
                val date = sdf.parse(mushroom.dateTime.toString())
                val timeInMillis = date?.time
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable(
                            onClick = {
                                selectedMushroom = mushroom
                                showOptionsDialog = true
                            }
                        )
                ) {
                    Column {
                        Text(text = "Nombre: ${mushroom.nombre}")
                        Text(text = "Descripcion: ${mushroom.descripcion}")
                        // Text(text = "Latitude: ${mushroom.latitud}")
                        // Text(text = "Longitude: ${mushroom.longitud}")
                        Text(text = "Fecha: ${
                            SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date(timeInMillis ?: 0))
                        }") // Muestra la fecha en formato dd-MM-yyyy HH:mm
                        val imageBitmap = decodeBase64ToBitmap(mushroom.encodedImage!!)
                        Image(
                            bitmap = imageBitmap.asImageBitmap(),
                            contentDescription = null, // alt text
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        if (showOptionsDialog) {
            AlertDialog(
                onDismissRequest = { showOptionsDialog = false },
                title = { Text("Options") },
                text = { Text("Do you want to edit or delete the mushroom?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showOptionsDialog = false
                            showEditDialog = true
                        }
                    ) {
                        Text("Edit")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showOptionsDialog = false
                            showDialog = true
                        }
                    ) {
                        Text("Delete")
                    }
                }
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Eliminar seta") },
                text = { Text("¿Quieres eliminar la seta ${selectedMushroom?.nombre}?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            selectedMushroom?.let { SetaManager.removeSeta(it) }
                        }
                    ) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDialog = false }
                    ) {
                        Text("No")
                    }
                }
            )
        }

        if (showEditDialog) {
            var newNombre by remember { mutableStateOf(selectedMushroom?.nombre ?: "") }
            var newDescripcion by remember { mutableStateOf(selectedMushroom?.descripcion ?: "") }
            var newLatitud by remember { mutableStateOf(selectedMushroom?.latitud.toString()) }
            var newLongitud by remember { mutableStateOf(selectedMushroom?.longitud.toString()) }

            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text("Editar seta") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newNombre,
                            onValueChange = { newNombre = it },
                            label = { Text("Nombre") }
                        )
                        OutlinedTextField(
                            value = newDescripcion,
                            onValueChange = { newDescripcion = it },
                            label = { Text("Descripcion") }
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showEditDialog = false
                            selectedMushroom?.let { mushroom ->
                                val updatedMushroom = mushroom.copy(
                                    nombre = newNombre,
                                    descripcion = newDescripcion,
                                    latitud = newLatitud.toDoubleOrNull() ?: mushroom.latitud,
                                    longitud = newLongitud.toDoubleOrNull() ?: mushroom.longitud
                                )
                                SetaManager.updateSeta(updatedMushroom)
                                    .addOnSuccessListener { updatedSeta ->
                                        Toast.makeText(context, "Seta actualizada", Toast.LENGTH_SHORT).show()
                                        (context as ListActivity).recreate() // Recargar la actividad después de la actualización
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(context, "Error al actualizar la seta: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                    ) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showEditDialog = false }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        // boton para volver a MainActivity
        Button(onClick = {
            context.startActivity(Intent(context, MushroomActivity::class.java))
        }) {
            Text(text = "Volver")
        }
    }
}