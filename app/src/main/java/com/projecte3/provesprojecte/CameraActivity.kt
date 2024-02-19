package com.projecte3.provesprojecte

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class CameraActivity : AppCompatActivity() {
    private var cameraPreview: CameraPreview? = null
    private var locationManager: LocationManager? = null
    private var location: Location? = null

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            this@CameraActivity.location = location
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        val previewFrame = findViewById<FrameLayout>(R.id.cameraPreview)
        cameraPreview = CameraPreview(this)
        previewFrame.addView(cameraPreview)

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val saveButton = findViewById<Button>(R.id.saveButton)

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

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val description = descriptionEditText.text.toString()

            // Save the data
            saveData(name, description)
        }
    }

    private fun saveData(name: String, description: String) {
        val seta = Seta(name, description, location?.latitude ?: 0.0, location?.longitude ?: 0.0)
        SetaManager.addSeta(seta)
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        cameraPreview?.surfaceDestroyed(cameraPreview!!.holder)
    }

    override fun onResume() {
        super.onResume()
        cameraPreview?.surfaceCreated(cameraPreview!!.holder)
    }
}