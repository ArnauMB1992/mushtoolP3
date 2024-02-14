package com.projecte3.provesprojecte

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.projecte3.provesprojecte.ui.theme.ProvesProjecte3Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity2 : ComponentActivity() {

    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private val _image = MutableLiveData<Bitmap>()
    val image: LiveData<Bitmap> get() = _image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraExecutor = Executors.newSingleThreadExecutor()
        requestPermissions()

        setContent {
            ProvesProjecte3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupPreview()
                }
            }
        }
    }

    @Composable
    fun SetupPreview() {
        val previewView = remember {
            PreviewView(this).apply {
                // Set up any additional configuration for your PreviewView

                // Configuración del Executor para la manipulación de la imagen
                implementationMode = PreviewView.ImplementationMode.PERFORMANCE
            }
        }

        Greeting2(image, this::takePhoto, this::goBack, previewView)
    }

    private fun requestPermissions() {
        if (allPermissionGranted()) {
            startCamera()
        } else {
            Toast.makeText(this, "Please Allow the Permission", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera() {
        val processCameraProvider = ProcessCameraProvider.getInstance(this)
        processCameraProvider.addListener({
            val cameraProvider = processCameraProvider.get()
            imageCapture = ImageCapture.Builder().build()

            lifecycleScope.launch(Dispatchers.Main) {
                cameraProvider.bindToLifecycle(
                    this@MainActivity2,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    imageCapture
                )
            }
        }, cameraExecutor)
    }

    private fun allPermissionGranted() = arrayOf(android.Manifest.permission.CAMERA).all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            getOutputDirectory(),
            SimpleDateFormat(
                "yyyy-MM-dd-HH-mm-ss-SSS",
                Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                    previewPhoto(savedUri)
                    Log.d("asdsadasd", "Photo capture succeeded: $savedUri")

                    // Abrir MainActivity3 con la imagen capturada
                    openMainActivity3(savedUri)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                }
            }
        )
    }

    private fun openMainActivity3(savedUri: Uri) {
        val intent = Intent(this, MainActivity3::class.java).apply {
            putExtra("imageUri", savedUri.toString())
        }
        startActivity(intent)
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun previewPhoto(uri: Uri) {
        lifecycleScope.launch(Dispatchers.IO) {
            val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
            withContext(Dispatchers.Main) {
                _image.value = bitmap
            }
        }
    }

    @Composable
    fun Greeting2(
        image: LiveData<Bitmap>,
        takePhoto: () -> Unit,
        goBack: () -> Unit,
        previewView: PreviewView,
        modifier: Modifier = Modifier
    ) {
        Column {
            Button(onClick = { goBack() }) {
                Text(text = "Go Back", fontSize = 24.sp)
            }

            Button(onClick = { takePhoto() }) {
                Text(text = "Take Photo", fontSize = 24.sp)
            }

            val img: Bitmap? by rememberUpdatedState(newValue = image.value)

            img?.let {
                ImageViewer(it)
            }

            AndroidView(
                factory = { context ->
                    previewView
                },
                update = { view ->
                    // Update the view if needed
                }
            )
        }
    }

    @Composable
    fun ImageViewer(image: Bitmap) {
        Image(
            bitmap = image.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

    private fun goBack() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseCamera()
    }

    private fun releaseCamera() {
        imageCapture = null
    }
}