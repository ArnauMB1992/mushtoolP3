package com.projecte3.provesprojecte

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

class MapActivity : ComponentActivity(), MapListener {

    lateinit var map: MapView
    lateinit var controller: IMapController
    lateinit var mMyLocationOverlay: MyLocationNewOverlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Establecer el contenido de la vista antes de intentar encontrar la vista
        setContentView(R.layout.activity_map)

        map = findViewById(R.id.map)

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

        val mapEventsOverlay = MapEventsOverlay(object : MapEventsReceiver {

            val TOLERANCE = 0.01 // Radio de tolerancia en grados

            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                p?.let {
                    val closestSeta = findClosestSeta(it)
                    closestSeta?.let { seta ->
                        val distance = haversine(p.latitude, p.longitude, seta.latitud!!, seta.longitud!!)
                        if (distance < TOLERANCE) {
                            val message = "Nombre: ${seta.nombre}, Descripción: ${seta.descripcion}"
                            Toast.makeText(this@MapActivity, message, Toast.LENGTH_LONG).show()
                            return true
                        }
                    }
                }
                return false
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                p?.let {
                    val closestSeta = findClosestSeta(it)
                    closestSeta?.let { seta ->
                        val distance = haversine(p.latitude, p.longitude, seta.latitud!!, seta.longitud!!)
                        if (distance < TOLERANCE) {
                            showEditDialog(seta)
                            return true
                        }
                    }
                }
                return false
            }
        })
        map.overlays.add(0, mapEventsOverlay)

        map.overlays.add(mMyLocationOverlay)

        map.addMapListener(this)

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, MushroomActivity::class.java)
            startActivity(intent)
        }

        // Obtener publicaciones de Firestore y agregarlas como marcadores
        fetchPostsAndAddMarkers()
    }

    // Función para encontrar la seta más cercana a un punto dado
    private fun findClosestSeta(p: GeoPoint): Seta? {
        var closestSeta: Seta? = null
        var minDistance = Double.MAX_VALUE
        for (seta in SetaManager.setas) {
            val distance = haversine(p.latitude, p.longitude, seta.latitud!!, seta.longitud!!)
            if (distance < minDistance) {
                minDistance = distance
                closestSeta = seta
            }
        }
        return closestSeta
    }

    // Función para mostrar un cuadro de diálogo que permite al usuario editar el nombre de la seta
    private fun showEditDialog(seta: Seta) {
        val editText = EditText(this)
        editText.setText(seta.nombre)
        AlertDialog.Builder(this)
            .setTitle("Editar nombre de la seta")
            .setView(editText)
            .setPositiveButton("Guardar") { _, _ ->
                val newName = editText.text.toString()
                if (newName.isNotBlank()) {
                    seta.nombre = newName
                    SetaManager.updateSeta(seta)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Nombre de la seta actualizado", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al actualizar el nombre de la seta: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun fetchPostsAndAddMarkers() {
        val postsRef = FirebaseDatabase.getInstance().getReference("post")
        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val seta = snapshot.getValue(Seta::class.java)
                    seta?.let {
                        val location = it.locationSerialized?.split(",")
                        val lat = location?.get(0)?.toDouble()
                        val lon = location?.get(1)?.toDouble()
                        val marker = Marker(map)

                        marker.icon = ContextCompat.getDrawable(this@MapActivity, R.drawable.seta)
                        marker.position = GeoPoint(lat!!, lon!!)
                        marker.title = it.nombre
                        map.overlays.add(marker)
                        map.invalidate() // Refrescar el mapa para mostrar el nuevo marcador
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors.
                Log.w("MapActivity", "Error al obtener documentos.", databaseError.toException())
            }
        })
    }

    private fun addMarkerForPost(post: Seta) {
        val marker = Marker(map)
        marker.position = GeoPoint(post.latitud!!, post.longitud!!)
        marker.icon = ContextCompat.getDrawable(this, R.drawable.seta)
        marker.title = post.nombre
        map.overlays.add(marker)
        map.invalidate() // Refrescar el mapa para mostrar el nuevo marcador

        // Agregar un mensaje de registro
        Log.d("MapActivity", "Marcador agregado: ${post.nombre} en (${post.latitud}, ${post.longitud})")
    }

    fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371 // radio de la tierra en km
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = Math.sin(latDistance / 2).pow(2.0) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(lonDistance / 2).pow(2.0)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return r * c
    }

    override fun onScroll(event: ScrollEvent?): Boolean {
        return true
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        return false
    }
}