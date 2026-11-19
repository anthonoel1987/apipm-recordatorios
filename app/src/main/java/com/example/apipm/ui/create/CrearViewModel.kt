package com.example.apipm.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.apipm.Categoria
import com.example.apipm.DatosSimulados
import com.example.apipm.Prioridad
import com.example.apipm.Recordatorio
import com.example.apipm.data.RecordatorioRepositorio
import com.example.apipm.data.Repositorios
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class CrearViewModel(
    private val repositorio: RecordatorioRepositorio,
    // Opciones definidas: la categoría elegida debe pertenecer a este conjunto.
    private val categoriasDisponibles: Set<Categoria> = DatosSimulados.categorias
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CrearUiState(
            opcionesCategoria = categoriasDisponibles.toList(),
            opcionesPrioridad = listOf(Prioridad.Baja, Prioridad.Media, Prioridad.Alta)
        )
    )
    val uiState: StateFlow<CrearUiState> = _uiState.asStateFlow()

    // Eventos que envía la interfaz

    fun onTituloChange(valor: String) {
        _uiState.update {
            it.copy(titulo = valor, validacionVisible = true, errorGuardado = null).validado()
        }
    }

    fun onDescripcionChange(valor: String) {
        _uiState.update {
            it.copy(descripcion = valor, validacionVisible = true, errorGuardado = null).validado()
        }
    }

    fun onCategoriaSeleccionada(categoria: Categoria) {
        _uiState.update {
            it.copy(categoria = categoria, validacionVisible = true, errorGuardado = null).validado()
        }
    }

    fun onPrioridadSeleccionada(prioridad: Prioridad) {
        _uiState.update {
            it.copy(prioridad = prioridad, validacionVisible = true, errorGuardado = null).validado()
        }
    }

    // Guarda el recordatorio. También se usa como acción de reintento.
    fun guardar() {
        val actual = _uiState.value
        if (!actual.puedeGuardar) return
        val categoria = actual.categoria ?: return
        val prioridad = actual.prioridad ?: return

        _uiState.update { it.copy(enviando = true, errorGuardado = null) }

        viewModelScope.launch {
            try {
                // El formulario ya está validado, por eso el require del modelo no falla.
                val nuevo = Recordatorio(
                    id = 0, // el repositorio asigna el id definitivo
                    titulo = actual.titulo.trim(),
                    descripcion = actual.descripcion.trim().ifBlank { null },
                    categoria = categoria,
                    prioridad = prioridad
                )
                repositorio.guardarRecordatorio(nuevo)
                _uiState.update { it.copy(enviando = false, guardadoExitoso = true) }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(
                        enviando = false,
                        errorGuardado = e.message ?: "No se pudo guardar el recordatorio"
                    )
                }
            }
        }
    }

    // Reglas de validación

    private fun CrearUiState.validado(): CrearUiState {
        val todos = validar(this)
        return copy(
            errores = if (validacionVisible) todos else emptyMap(),
            formularioValido = todos.isEmpty()
        )
    }

    private fun validar(estado: CrearUiState): Map<CampoFormulario, String> {
        val errores = mutableMapOf<CampoFormulario, String>()

        if (estado.titulo.trim().length < TITULO_MINIMO) {
            errores[CampoFormulario.TITULO] =
                "El título debe tener al menos $TITULO_MINIMO caracteres."
        }

        if (estado.descripcion.length > DESCRIPCION_MAXIMA) {
            errores[CampoFormulario.DESCRIPCION] =
                "La descripción no puede superar los $DESCRIPCION_MAXIMA caracteres " +
                        "(tiene ${estado.descripcion.length})."
        }

        val categoria = estado.categoria
        if (categoria == null || categoria !in categoriasDisponibles) {
            errores[CampoFormulario.CATEGORIA] = "Selecciona una categoría de la lista."
        }

        if (estado.prioridad == null) {
            errores[CampoFormulario.PRIORIDAD] = "Selecciona una prioridad."
        }

        return errores
    }

    companion object {
        const val TITULO_MINIMO = 4
        const val DESCRIPCION_MAXIMA = 200

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { CrearViewModel(Repositorios.recordatorios) }
        }
    }
}