package com.projecte3.provesprojecte

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.threeten.bp.LocalDate
import org.threeten.bp.format.TextStyle
import java.io.IOException
import java.util.Locale

class WeatherActivity : AppCompatActivity() {
    private val client = OkHttpClient()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            this.WeatherScreen()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun WeatherScreen() {
        val weatherDescription = remember { mutableStateOf("") }
        val temperature = remember { mutableStateOf("") }
        val location = remember { mutableStateOf("") }
        val forecast = remember { mutableStateOf(listOf<String>()) }
        val scope = rememberCoroutineScope()

        // Update the UI
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Display the weather image
                val weatherImage = when {
                    listOf("sol", "soleado", "cla", "des").any { weatherDescription.value.lowercase(Locale("es")).contains(it) } -> R.raw.sunny
                    listOf("nub", "nubes").any { weatherDescription.value.lowercase(Locale("es")).contains(it) } -> R.raw.nublado
                    listOf("lluv", "lluvia", "chu").any { weatherDescription.value.lowercase(Locale("es")).contains(it) } -> R.raw.rainy
                    else -> R.raw.default_weather
                }

                GlideImage(
                    data = weatherImage,
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                )

                TextField(
                    value = location.value,
                    onValueChange = { location.value = it },
                    label = { Text("Introduce la ubicación") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Descripción del clima: ${weatherDescription.value}")
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Temperatura: ${temperature.value} °C")
                Spacer(modifier = Modifier.height(16.dp))

                // Get the current day of the week
                val currentDayOfWeek = LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es")).capitalize(Locale("es"))

                // Create a list of the days of the week starting from the current day
                val daysOfWeek = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
                val startIndex = daysOfWeek.indexOf(currentDayOfWeek)
                val adjustedDaysOfWeek = daysOfWeek.drop(startIndex) + daysOfWeek.take(startIndex)

                Column(modifier = Modifier.weight(1f)) {
                    for (i in 1 until 5) { // Only show the next 4 days
                        if (forecast.value.size > i) {
                            val dayForecast = forecast.value[i]
                            Text(text = "${adjustedDaysOfWeek[i]}:\n$dayForecast")
                        } else {
                            Text(text = "${adjustedDaysOfWeek[i]}: Cargando...")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    val context = LocalContext.current
                    Button(onClick = {
                        if (location.value.isNotEmpty()) {
                            scope.launch {
                                fetchWeatherData(weatherDescription, temperature, location.value)
                                fetchForecastData(forecast, location.value) // Fetch the forecast data
                            }
                        }
                    }, modifier = Modifier.weight(1f)) {
                        Text("Consultar")
                    }
                    Spacer(modifier = Modifier.width(16.dp)) // Agrega un espacio entre los botones
                    Button(onClick = {
                        context.startActivity(Intent(context, MainActivity::class.java))
                    }, modifier = Modifier.weight(1f)) {
                        Text(text = "Volver", color = Color.White)
                    }
                }
            }
        }
    }

    @Composable
    private fun GlideImage(data: Int, modifier: Modifier) {
        val context = LocalContext.current
        val imageView = remember { ImageView(context) }
        Glide.with(context).load(data).into(imageView)

        AndroidView({ imageView }, modifier = modifier, update = {})
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
                Log.e("WeatherActivity", "Error al obtener los datos del clima", e)
            }
        }
    }

    private suspend fun fetchForecastData(forecast: MutableState<List<String>>, location: String) {
        val apiKey = BuildConfig.OpenWeatherApiKey
        val request = Request.Builder()
            .url("http://api.openweathermap.org/data/2.5/forecast?q=$location&appid=$apiKey&lang=es&cnt=40")
            .build()

        withContext(Dispatchers.IO) {
            try {
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw IOException("Código inesperado $response")

                    val responseData = response.body?.string()
                    val json = JSONObject(responseData ?: "")
                    val forecastJson = json.getJSONArray("list")

                    // Update the state
                    val forecastList = mutableListOf<String>()
                    for (i in 0 until forecastJson.length() step 8) {
                        val dayForecastJson = forecastJson.getJSONObject(i)
                        val temperatureJson = dayForecastJson.getJSONObject("main")
                        val weatherDescriptionJson = dayForecastJson.getJSONArray("weather").getJSONObject(0)

                        val dayForecast = "Clima: ${weatherDescriptionJson.getString("description")}\nTemperatura: ${((temperatureJson.getDouble("temp") - 273.15).toInt()).toString()} °C\n"
                        forecastList.add(dayForecast)
                    }
                    forecast.value = forecastList
                }
            } catch (e: IOException) {
                // Handle the exception here
                Log.e("WeatherActivity", "Error al obtener los datos del pronóstico", e)
            }
        }
    }
}