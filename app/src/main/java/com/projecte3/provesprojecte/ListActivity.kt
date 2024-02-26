package com.projecte3.provesprojecte

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

class ListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SetaManager.loadSetas(this)
        val mushrooms = SetaManager.setas
        setContent {
            MushroomListScreen(mushrooms, this)
        }
    }
}

fun decodeBase64ToBitmap(base64String: String): Bitmap {
    val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

@Composable
fun MushroomListScreen(mushrooms: List<Seta>, context: Context) {
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showOptionsDialog by remember { mutableStateOf(false) }
    var selectedMushroom by remember { mutableStateOf<Seta?>(null) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn {
            items(mushrooms) { mushroom ->
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
                        Text(text = "Fecha: ${mushroom.dateTime}") // Muestra la fecha

                        val imageBitmap = decodeBase64ToBitmap(mushroom.image)
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
                            selectedMushroom?.let { SetaManager.removeSeta(it, context) }
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
                        OutlinedTextField(
                            value = newLatitud,
                            onValueChange = { newLatitud = it },
                            label = { Text("Latitud") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        OutlinedTextField(
                            value = newLongitud,
                            onValueChange = { newLongitud = it },
                            label = { Text("Longitud") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
                                SetaManager.setas = SetaManager.setas.map { if (it == mushroom) updatedMushroom else it }
                                    .toMutableList()
                                SetaManager.saveSetas(context)
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
}