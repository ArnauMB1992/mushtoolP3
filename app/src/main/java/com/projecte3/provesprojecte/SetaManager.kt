package com.projecte3.provesprojecte

object SetaManager {
    fun addSeta(seta: Seta) {
        setas.add(seta)
    }

    fun removeSeta(seta: Seta) {
        setas.remove(seta)
    }

    val setas = mutableListOf(
        Seta("Robellon", "Comestible",41.561784, 1.994427),
        Seta("Camagroc", "Comestible", 41.569242, 1.996652),
        Seta("Pet de llob", "Comestible", 41.573745, 1.999281),
        Seta("Trompeta de la mort", "Comestible", 41.574571, 1.997189),
        Seta("Anell blanc", "Comestible", 41.573316, 2.002395)
    )
}