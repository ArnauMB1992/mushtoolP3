package com.projecte3.provesprojecte

import androidx.annotation.DrawableRes
import java.util.Date

data class Seta(
    var nombre: String,
    var descripcion: String,
    val latitud: Double,
    val longitud: Double,
    val dateTime: Date?,
    @DrawableRes val imageRes: Int,
)