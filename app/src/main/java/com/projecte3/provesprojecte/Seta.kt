package com.projecte3.provesprojecte

import java.io.Serializable
import java.util.Date

class Seta() : Serializable {
    var id: String? = null
    var nombre: String? = null
    var descripcion: String? = null
    var latitud: Double? = null
    var longitud: Double? = null
    var dateTime: Date? = null
    var encodedImage: String? = null

    constructor(id: String?, name: String, description: String, latitud: Double, longitud: Double, dateTime: Date?, encodedImage: String) : this() {
        this.id = id
        this.nombre = name
        this.descripcion = description
        this.latitud = latitud
        this.longitud = longitud
        this.dateTime = dateTime
        this.encodedImage = encodedImage
    }

    fun copy(id: String? = this.id, nombre: String, descripcion: String? = this.descripcion, latitud: Double? = this.latitud, longitud: Double? = this.longitud): Seta {
        val copy = Seta()
        copy.id = id
        copy.nombre = nombre
        copy.descripcion = descripcion
        copy.latitud = latitud
        copy.longitud = longitud
        return copy
    }
}