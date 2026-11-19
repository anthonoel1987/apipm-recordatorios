package com.example.apipm.ui.list

import com.example.apipm.Recordatorio

// Todos los estados posibles de la pantalla de listado.
// La pantalla solo dibuja lo que indique este estado.
sealed interface ListaUiState {
    data object Cargando : ListaUiState
    data object Vacio : ListaUiState
    data class Error(val mensaje: String) : ListaUiState
    data class Contenido(
        val filtro: Filtro,
        val recordatorios: List<Recordatorio>
    ) : ListaUiState
}

