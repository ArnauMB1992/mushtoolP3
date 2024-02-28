package com.projecte3.provesprojecte

import android.app.AlertDialog
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
import com.google.firebase.firestore.FirebaseFirestore
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
    private val db = FirebaseFirestore.getInstance()

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

        map.overlays.add(mMyLocationOverlay)

        // Crear un MapEventsReceiver
        val receiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                var closestSeta: Seta? = null
                var minDistance = Double.MAX_VALUE

                for (seta in SetaManager.setas) {
                    val distance = haversine(p.latitude, p.longitude, seta.latitud!!, seta.longitud!!)
                    if (distance < minDistance) {
                        minDistance = distance
                        closestSeta = seta
                    }
                }

                if (closestSeta != null) {
                    // Mostrar un mensaje con el nombre de la seta más cercana
                    Toast.makeText(this@MapActivity, "La seta más cercana es ${closestSeta.nombre}, es ${closestSeta.descripcion}", Toast.LENGTH_SHORT).show()
                }
                return true
            }

            // Eliminar todos los marcadores que se encuentren en la posición del punto en el que se hizo clic largo
            override fun longPressHelper(p: GeoPoint?): Boolean {
                if (p != null) {
                    var closestSeta = findClosestSeta(p)
                    if (closestSeta != null) {
                        val editText = EditText(this@MapActivity)
                        editText.setText(closestSeta!!.nombre)

                        AlertDialog.Builder(this@MapActivity)
                            .setTitle("Editar seta")
                            .setMessage("Introduce el nuevo nombre de la seta")
                            .setView(editText)
                            .setPositiveButton("Aceptar") { _, _ ->
                                val newName = editText.text.toString()
                                closestSeta = closestSeta!!.copy(nombre = newName)
                                // Actualizar el título del marcador en el mapa
                                val marker = map.overlays.firstOrNull { it is Marker && it.position.latitude == closestSeta!!.latitud && it.position.longitude == closestSeta!!.longitud } as? Marker
                                if (marker != null) {
                                    marker.title = newName
                                    map.invalidate() // Refrescar el mapa para mostrar el nuevo nombre en el marcador
                                }

                                // Actualizar el nombre de la seta en la lista de setas
                                val setaIndex = SetaManager.setas.indexOfFirst { it.latitud == closestSeta!!.latitud && it.longitud == closestSeta!!.longitud }
                                if (setaIndex != -1) {
                                    SetaManager.setas[setaIndex] = closestSeta!!
                                    // Los cambios en los objetos de Seta se realizan directamente en Firebase
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
                    val distance = haversine(p.latitude, p.longitude, seta.latitud!!, seta.longitud!!)
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
        // loadMarkerPreferences() // This line is commented out because the function is not defined

        map.addMapListener(this)

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        // Obtener publicaciones de Firestore y agregarlas como marcadores
        fetchPostsAndAddMarkers()
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