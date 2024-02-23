package com.projecte3.provesprojecte

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.util.Calendar
import java.util.Date

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

    @RequiresApi(Build.VERSION_CODES.O)
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

            // Get current date and time
            val calendar = Calendar.getInstance()
            val dateTime = calendar.time

            // Check if location is available
            if (location != null) {
                saveData(name, description, location!!.latitude, location!!.longitude, dateTime)
            } else {
                // If location is not available, wait for it
                val handler = Handler(Looper.getMainLooper())
                val runnable = object : Runnable {
                    override fun run() {
                        if (location != null) {
                            saveData(name, description, location!!.latitude, location!!.longitude, dateTime)
                        } else {
                            // If location is still not available, wait another second
                            handler.postDelayed(this, 1000)
                        }
                    }
                }
                // Start waiting
                handler.postDelayed(runnable, 1000)
            }
        }
    }

    private fun saveData(name: String, description: String, latitude: Double, longitude: Double, dateTime: Date?) {
        val seta = Seta(name, description, latitude, longitude, dateTime, R.drawable.agaricuscampestris)
        SetaManager.addSeta(seta, this)
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