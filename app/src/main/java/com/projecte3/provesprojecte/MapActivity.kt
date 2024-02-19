package com.projecte3.provesprojecte

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import kotlin.math.pow

data class Seta(
    var nombre: String,
    var descripcion: String,
    val latitud: Double,
    val longitud: Double
)

class MapActivity : ComponentActivity(), MapListener {

    lateinit var map: MapView
    lateinit var controller: IMapController
    lateinit var mMyLocationOverlay: MyLocationNewOverlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view before trying to find the view
        setContentView(R.layout.activity_map)

        map = findViewById(R.id.map)

        for (seta in SetaManager.setas) {
            val marker = Marker(map)
            marker.position = GeoPoint(seta.latitud, seta.longitud)
            marker.icon = ContextCompat.getDrawable(this, R.drawable.seta)
            marker.title = seta.nombre
            map.overlays.add(marker)
        }

        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        )

        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        mMyLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), map)
        controller = map.controller

        mMyLocationOverlay.enableMyLocation()
        mMyLocationOverlay.enableFollowLocation()
        mMyLocationOverlay.isDrawAccuracyEnabled = true
        mMyLocationOverlay.runOnFirstFix {
            runOnUiThread {
                controller.setCenter(mMyLocationOverlay.myLocation)
                controller.animateTo(mMyLocationOverlay.myLocation)
            }
        }
        controller.setZoom(10.0)

        map.overlays.add(mMyLocationOverlay)

        // Crear un MapEventsReceiver
        val receiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                var closestSeta: Seta? = null
                var minDistance = Double.MAX_VALUE

                for (seta in SetaManager.setas) {
                    val distance = haversine(p.latitude, p.longitude, seta.latitud, seta.longitud)
                    if (distance < minDistance) {
                        minDistance = distance
                        closestSeta = seta
                    }
                }

                if (closestSeta != null) {
                    // Muestra un mensaje con el nombre de la seta más cercana
                    Toast.makeText(this@MapActivity, "La seta más cercana es ${closestSeta.nombre}, es ${closestSeta.descripcion}", Toast.LENGTH_SHORT).show()
                }

                return true
            }

            // Eliminar todos los marcadores que se encuentren en la posición del punto en el que se hizo clic largo
            override fun longPressHelper(p: GeoPoint?): Boolean {
                if (p != null) {
                    val closestSeta = findClosestSeta(p)
                    if (closestSeta != null) {
                        val editText = EditText(this@MapActivity)
                        editText.setText(closestSeta.nombre)

                        AlertDialog.Builder(this@MapActivity)
                            .setTitle("Editar seta")
                            .setMessage("Introduce el nuevo nombre de la seta")
                            .setView(editText)
                            .setPositiveButton("Aceptar") { _, _ ->
                                val newName = editText.text.toString()
                                closestSeta.nombre = newName
                                // Actualiza el título del marcador en el mapa
                                val marker = map.overlays.firstOrNull { it is Marker && it.position.latitude == closestSeta.latitud && it.position.longitude == closestSeta.longitud } as? Marker
                                if (marker != null) {
                                    marker.title = newName
                                    map.invalidate() // Refresca el mapa para mostrar el nuevo nombre en el marcador
                                }
                            }
                            .setNegativeButton("Cancelar", null)
                            .show()
                    }
                }

                return true
            }

            fun findClosestSeta(p: GeoPoint): Seta? {
                var closestSeta: Seta? = null
                var minDistance = Double.MAX_VALUE

                for (seta in SetaManager.setas) {
                    val distance = haversine(p.latitude, p.longitude, seta.latitud, seta.longitud)
                    if (distance < minDistance) {
                        minDistance = distance
                        closestSeta = seta
                    }
                }

                return closestSeta
            }
        }

        // Crear un MapEventsOverlay y agregarlo al mapa
        val overlay = MapEventsOverlay(receiver)
        map.overlays.add(overlay)

        // Cargar los marcadores guardados
        loadMarkerPreferences()

        map.addMapListener(this)

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }
    }

    fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371 // radius of the earth in km
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = Math.sin(latDistance / 2).pow(2.0) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(lonDistance / 2).pow(2.0)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return r * c
    }

    private fun loadMarkerPreferences() {
        val sharedPreferences = getSharedPreferences("markers", MODE_PRIVATE)
        val existingMarkers = sharedPreferences.getStringSet("locations", mutableSetOf()) ?: mutableSetOf()
        for (location in existingMarkers) {
            val latLon = location.split(",")
            val lat = latLon[0].toDouble()
            val lon = latLon[1].toDouble()
            val marker = Marker(map)
            marker.position = GeoPoint(lat, lon)
            marker.icon = ContextCompat.getDrawable(this, R.drawable.seta)
            map.overlays.add(marker)
        }
    }

    private fun deleteMarkerPreferences() {
        val sharedPreferences = getSharedPreferences("markers", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    override fun onScroll(event: ScrollEvent?): Boolean {
        return true
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        return false
    }
}