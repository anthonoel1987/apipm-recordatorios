package com.example.apipm.data

import com.example.apipm.DatosSimulados
import com.example.apipm.Recordatorio
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger

// Implementación en memoria: los datos viven solo mientras la app está abierta.
class RecordatorioRepositorioEnMemoria(
    recordatoriosIniciales: List<Recordatorio> = DatosSimulados.recordatorios
) : RecordatorioRepositorio {

    private val recordatorios = MutableStateFlow(recordatoriosIniciales)

    private val _modoSimulacion = MutableStateFlow(ModoSimulacion.NORMAL)
    override val modoSimulacion: StateFlow<ModoSimulacion> = _modoSimulacion.asStateFlow()

    private val ultimoId = AtomicInteger(recordatoriosIniciales.maxOfOrNull { it.id } ?: 0)

    override fun observarRecordatorios(): Flow<List<Recordatorio>> = flow {
        delay(RETARDO_MS) // simula el tiempo de una carga real
        if (_modoSimulacion.value == ModoSimulacion.ERROR_CARGA) {
            throw IOException("Error simulado: no se pudieron cargar los recordatorios")
        }
        emitAll(
            combine(recordatorios, _modoSimulacion) { lista, modo ->
                if (modo == ModoSimulacion.VACIO) emptyList<Recordatorio>() else lista
            }
        )
    }

    override suspend fun obtenerRecordatorio(id: Int): Recordatorio? {
        delay(RETARDO_CORTO_MS)
        if (_modoSimulacion.value == ModoSimulacion.ERROR_CARGA) {
            throw IOException("Error simulado: no se pudo cargar el recordatorio")
        }
        return recordatorios.value.firstOrNull { it.id == id }
    }

    override suspend fun guardarRecordatorio(recordatorio: Recordatorio): Recordatorio {
        delay(RETARDO_MS) // simula el tiempo de un guardado real
        if (_modoSimulacion.value == ModoSimulacion.ERROR_GUARDADO) {
            throw IOException("Error simulado: no se pudo guardar el recordatorio")
        }
        val guardado = recordatorio.copy(id = ultimoId.incrementAndGet())
        recordatorios.update { actual -> actual + guardado }
        return guardado
    }

    override fun cambiarModoSimulacion(modo: ModoSimulacion) {
        _modoSimulacion.value = modo
    }

    private companion object {
        const val RETARDO_MS = 1500L
        const val RETARDO_CORTO_MS = 500L
    }
}
