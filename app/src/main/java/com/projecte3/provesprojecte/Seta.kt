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
    var locationSerialized: String? = null

    constructor(id: String?, name: String, description: String, latitud: Double, longitud: Double, dateTime: Date?, encodedImage: String) : this() {
        this.id = id
        this.nombre = name
        this.latitud = latitud
        this.dateTime = dateTime
        this.longitud = longitud
        this.descripcion = description
        this.encodedImage = encodedImage
        this.locationSerialized = "$latitud,$longitud"
    }

    fun copy(id: String? = this.id, nombre: String, descripcion: String? = this.descripcion, latitud: Double? = this.latitud, longitud: Double? = this.longitud): Seta {
        val copy = Seta()
        copy.id = id
        copy.nombre = nombre
        copy.descripcion = descripcion
        copy.latitud = latitud
        copy.longitud = longitud
        copy.locationSerialized = this.locationSerialized
        return copy
    }
}