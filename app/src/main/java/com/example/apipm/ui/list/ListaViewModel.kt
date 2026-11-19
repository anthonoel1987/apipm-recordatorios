package com.example.apipm.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.apipm.Recordatorio
import com.example.apipm.data.ModoSimulacion
import com.example.apipm.data.RecordatorioRepositorio
import com.example.apipm.data.Repositorios
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModel(
    private val repositorio: RecordatorioRepositorio
) : ViewModel() {

    private val filtroSeleccionado = MutableStateFlow(Filtro.TODOS)

    // Cada vez que cambia este contador se vuelve a cargar la lista (reintento).
    private val reintentos = MutableStateFlow(0)

    val uiState: StateFlow<ListaUiState> = reintentos
        .flatMapLatest {
            repositorio.observarRecordatorios()
                .combine(filtroSeleccionado) { lista, filtro -> construirEstado(lista, filtro) }
                .onStart { emit(ListaUiState.Cargando) }
                .catch { error ->
                    emit(ListaUiState.Error(error.message ?: "Ocurrió un error inesperado"))
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ListaUiState.Cargando
        )

    val modoSimulacion: StateFlow<ModoSimulacion> = repositorio.modoSimulacion

    private fun construirEstado(lista: List<Recordatorio>, filtro: Filtro): ListaUiState =
        if (lista.isEmpty()) {
            ListaUiState.Vacio
        } else {
            ListaUiState.Contenido(filtro = filtro, recordatorios = filtro.aplicar(lista))
        }

    fun seleccionarFiltro(filtro: Filtro) {
        filtroSeleccionado.value = filtro
    }

    fun reintentar() {
        reintentos.update { it + 1 }
    }

    // Cambia el modo de simulación y recarga para ver el resultado de inmediato.
    fun cambiarModoSimulacion(modo: ModoSimulacion) {
        repositorio.cambiarModoSimulacion(modo)
        reintentar()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { ListaViewModel(Repositorios.recordatorios) }
        }
    }
}

