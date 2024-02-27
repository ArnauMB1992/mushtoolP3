package com.projecte3.provesprojecte

import java.util.Date

data class Seta(
    var nombre: String,
    var descripcion: String,
    val latitud: Double,
    val longitud: Double,
    val dateTime: Date?,
    val image: String,
)