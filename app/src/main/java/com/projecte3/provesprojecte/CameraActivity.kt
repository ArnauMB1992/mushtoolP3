package com.projecte3.provesprojecte

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.util.Calendar

class CameraActivity : AppCompatActivity() {
    private var locationManager: LocationManager? = null
    private var location: Location? = null
    private var imageBitmap: Bitmap? = null

    private val REQUEST_IMAGE_CAPTURE = 1

    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("post")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val captureButton = findViewById<Button>(R.id.captureButton)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        captureButton.setOnClickListener {
            // Check for location permissions
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Get the location before starting the camera intent
                location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent.resolveActivity(packageManager) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            } else {
                Toast.makeText(this, "Porfavor activa los permisos de ubicación", Toast.LENGTH_SHORT).show()
            }
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val description = descriptionEditText.text.toString()

            // Get current date and time
            val calendar = Calendar.getInstance()
            val dateTime = calendar.time

            // Check if location and image are available
            if (location != null && imageBitmap != null) {
                val newSetaId = myRef.push().key
                val seta = Seta(newSetaId, name, description, location!!.latitude, location!!.longitude, dateTime, encodeImageToBase64(imageBitmap!!))
                myRef.child(newSetaId!!).setValue(seta)
                Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show()

                // Reproduce el sonido de guardado
                val mediaPlayer = MediaPlayer.create(this, R.raw.save)
                mediaPlayer.start()

                // Iniciar MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Porfavor captura una imagen y asegurate de que tengas activado el gps", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun encodeImageToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            Toast.makeText(this, "Captura realizada", Toast.LENGTH_SHORT).show()

            // Obtener la ubicación actual después de capturar la foto
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                Toast.makeText(this, "Ubicación guardada", Toast.LENGTH_SHORT).show()
            }
        }
    }
}