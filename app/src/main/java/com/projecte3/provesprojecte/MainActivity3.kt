package com.projecte3.provesprojecte

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.File
import java.io.FileOutputStream

class MainActivity3 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background

            ) {
                ImageScreen()
            }
        }
    }
}

@Composable
fun ImageScreen() {
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val imageBitmap = remember { loadImageBitmap() }
    var name by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier.aspectRatio(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            imageBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter Name") }
        )

        Button(
            onClick = {
                if (name.isNotEmpty() && imageBitmap != null) {
                    savePhotoWithCustomName(context, imageBitmap, name)
                    finishActivity(context, activity)
                }
            },
            enabled = imageBitmap != null && name.isNotEmpty()
        ) {
            Text("Save with Name")
        }
    }
}
fun loadImageBitmap(): Bitmap? {
    val imagePath = getImageFilePath()
    return if (imagePath != null) {
        val imageFile = File(imagePath)
        if (imageFile.exists()) {
            decodeSampledBitmapFromFile(imageFile, 200, 200) // Adjust width and height as needed// Adjust width and height as needed
        } else {
            null
        }
    } else {
        null
    }
}
fun decodeSampledBitmapFromFile(file: File, reqWidth: Int, reqHeight: Int): Bitmap? {
    // Primero decodificamos con inJustDecodeBounds=true para verificar las dimensiones
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    BitmapFactory.decodeFile(file.path, options)

    // Calculamos inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

    // Decodificamos bitmap con inSampleSize ajustado
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeFile(file.path, options)
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Altura y anchura de la imagen original
    val height: Int = options.outHeight
    val width: Int = options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {

        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        // Calculamos el mayor inSampleSize que es una potencia de 2 y mantiene tanto
        // la altura como la anchura mayor que las solicitadas.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}

fun getImageFilePath(): String? {
    // Aquí debes retornar la ruta de la imagen que deseas cargar.
    // Este es un ejemplo de cómo podrías hacerlo.
    val dir = Environment.getExternalStorageDirectory()
    return "${dir.absolutePath}/myImage.jpg"
}

fun savePhotoWithCustomName(context: Context, bitmap: Bitmap, name: String) {
    // Aquí debes guardar la imagen con el nombre personalizado.
    // Este es un ejemplo de cómo podrías hacerlo.
    val dir = Environment.getExternalStorageDirectory()
    val file = File(dir, "$name.jpg")
    val outStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
    outStream.flush()
    outStream.close()
}

fun finishActivity(context: Context, activity: Activity) {
    val intent = Intent(context, MainActivity::class.java)
    context.startActivity(intent)
    activity.finish()
}