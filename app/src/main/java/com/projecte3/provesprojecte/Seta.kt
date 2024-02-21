package com.projecte3.provesprojecte

import androidx.annotation.DrawableRes

data class Seta(
    var nombre: String,
    var descripcion: String,
    val latitud: Double,
    val longitud: Double,
    @DrawableRes val imageRes: Int,
)