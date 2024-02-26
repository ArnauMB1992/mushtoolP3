package com.projecte3.provesprojecte

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class WeatherActivity : AppCompatActivity() {
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherScreen()
        }
    }

    @Composable
    fun WeatherScreen() {
        val context = LocalContext.current
        val weatherDescription = remember { mutableStateOf("") }
        val temperature = remember { mutableStateOf("") }
        val location = remember { mutableStateOf("") }
        val scope = rememberCoroutineScope()

        // Update the UI
        Column(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = location.value,
                onValueChange = { location.value = it },
                label = { Text("Introduce la ubicación") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (location.value.isNotEmpty()) {
                    scope.launch {
                        fetchWeatherData(weatherDescription, temperature, location.value)
                    }
                }
            }) {
                Text("Consultar")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, object : LocationListener {
                        override fun onLocationChanged(location: Location) {
                            scope.launch {
                                fetchWeatherData(weatherDescription, temperature, "${location.latitude},${location.longitude}")
                            }
                        }

                        override fun onProviderDisabled(provider: String) {
                            // This method is called when the provider is disabled by the user.
                            // You can leave it empty if you don't need to do anything in this case.
                        }
                    }, Looper.getMainLooper())
                }
            }) {
                Text("Tiempo en mi zona")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Descripción del clima: ${weatherDescription.value}")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Temperatura: ${temperature.value} °C")
        }
    }

    private suspend fun fetchWeatherData(weatherDescription: MutableState<String>, temperature: MutableState<String>, location: String) {
        val apiKey = BuildConfig.OpenWeatherApiKey
        val request = Request.Builder()
            .url("http://api.openweathermap.org/data/2.5/weather?q=$location&appid=$apiKey&lang=es")
            .build()

        withContext(Dispatchers.IO) {
            try {
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw IOException("Código inesperado $response")

                    val responseData = response.body?.string()
                    val json = JSONObject(responseData ?: "")
                    val weatherDescriptionJson = json.getJSONArray("weather").getJSONObject(0)
                    val temperatureJson = json.getJSONObject("main")

                    // Update the state
                    weatherDescription.value = weatherDescriptionJson.getString("description")
                    temperature.value = ((temperatureJson.getDouble("temp") - 273.15).toInt()).toString()
                }
            } catch (e: IOException) {
                // Handle the exception here
                // For example, you can log the error or show a message to the user
                Log.e("WeatherActivity", "Error al obtener los datos del clima", e)
            }
        }
    }
}