package com.projecte3.provesprojecte

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
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
import java.io.ByteArrayOutputStream
import java.util.Calendar
import java.util.Date

class CameraActivity : AppCompatActivity() {
    private var locationManager: LocationManager? = null
    private var location: Location? = null
    private var imageBitmap: Bitmap? = null

    private val REQUEST_IMAGE_CAPTURE = 1

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            this@CameraActivity.location = location
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val captureButton = findViewById<Button>(R.id.captureButton)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)
        }

        captureButton.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val description = descriptionEditText.text.toString()

            // Get current date and time
            val calendar = Calendar.getInstance()
            val dateTime = calendar.time

            // Check if location is available
            if (location != null && imageBitmap != null) {
                saveData(name, description, location!!.latitude, location!!.longitude, dateTime, imageBitmap!!)
            } else {
                Toast.makeText(this, "Please capture an image and ensure location is available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveData(name: String, description: String, latitude: Double, longitude: Double, dateTime: Date?, image: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT)

        val seta = Seta(name, description, latitude, longitude, dateTime, encodedImage)
        SetaManager.addSeta(seta, this)
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
        }
    }
}