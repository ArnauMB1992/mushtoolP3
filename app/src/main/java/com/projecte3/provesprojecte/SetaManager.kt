package com.projecte3.provesprojecte

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SetaManager {

    data class Seta(
        var nombre: String,
        var descripcion: String,
        val latitud: Double,
        val longitud: Double,
    )

    var setas = mutableListOf<Seta>()

    fun addSeta(seta: Seta, context: Context) {
        setas.add(seta)
        saveSetas(context)
    }

    fun removeSeta(seta: Seta, context: Context) {
        setas.remove(seta)
        saveSetas(context)
    }

    fun saveSetas(context: Context) {
        val sharedPreferences = context.getSharedPreferences("SetaManager", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(setas)
        editor.putString("setas", json)
        editor.apply()
    }

    fun loadSetas(context: Context) {
        val sharedPreferences = context.getSharedPreferences("SetaManager", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("setas", null)
        val type = object : TypeToken<List<Seta>>() {}.type
        setas = gson.fromJson(json, type) ?: mutableListOf()
    }
}