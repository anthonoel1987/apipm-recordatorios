package com.example.apipm.ui.detail

import com.example.apipm.Recordatorio

// Estados posibles de la pantalla de detalle.
sealed interface DetalleUiState {
    data object Cargando : DetalleUiState
    data object NoEncontrado : DetalleUiState
    data class Error(val mensaje: String) : DetalleUiState
    data class Exito(val recordatorio: Recordatorio) : DetalleUiState
}

