package com.example.apipm.navigation

// Rutas de la aplicación y su argumento.
object Rutas {
    const val LISTA = "lista"
    const val CREAR = "crear"

    // Argumento de la ruta de detalle: el id del recordatorio (Int).
    const val ARG_ID = "id"
    const val DETALLE = "detalle/{$ARG_ID}"

    fun detalle(id: Int): String = "detalle/$id"
}

