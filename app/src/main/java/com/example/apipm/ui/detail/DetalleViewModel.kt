package com.example.apipm.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.apipm.data.RecordatorioRepositorio
import com.example.apipm.data.Repositorios
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class DetalleViewModel(
    private val id: Int,
    private val repositorio: RecordatorioRepositorio
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetalleUiState>(DetalleUiState.Cargando)
    val uiState: StateFlow<DetalleUiState> = _uiState.asStateFlow()

    init {
        cargar()
    }

    // Carga el recordatorio; también se usa como acción de reintento.
    fun cargar() {
        viewModelScope.launch {
            _uiState.value = DetalleUiState.Cargando
            _uiState.value = try {
                val recordatorio = repositorio.obtenerRecordatorio(id)
                if (recordatorio == null) {
                    DetalleUiState.NoEncontrado
                } else {
                    DetalleUiState.Exito(recordatorio)
                }
            } catch (e: IOException) {
                DetalleUiState.Error(e.message ?: "No se pudo cargar el recordatorio")
            }
        }
    }

    companion object {
        // El id llega desde el argumento de la ruta.
        fun crearFactory(id: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer { DetalleViewModel(id, Repositorios.recordatorios) }
        }
    }
}