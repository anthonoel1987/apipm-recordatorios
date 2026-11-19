package com.example.apipm.data

import com.example.apipm.Recordatorio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

// Contrato de la fuente de datos. Los ViewModel dependen de esta interfaz
// y no de una implementación concreta, para poder reemplazarla más adelante
// (base de datos local, servicio remoto) sin modificar las pantallas.
interface RecordatorioRepositorio {

    // Emite la lista de recordatorios y vuelve a emitir cada vez que cambia.
    // Puede fallar con IOException.
    fun observarRecordatorios(): Flow<List<Recordatorio>>

    // Devuelve el recordatorio con ese id, o null si no existe.
    // Puede fallar con IOException.
    suspend fun obtenerRecordatorio(id: Int): Recordatorio?

    // Guarda un recordatorio nuevo y lo devuelve con el id asignado por el
    // repositorio (el id que llegue en el parámetro se ignora).
    // Puede fallar con IOException.
    suspend fun guardarRecordatorio(recordatorio: Recordatorio): Recordatorio

    // Mecanismo de simulación (consigna 3). Una fuente real lo ignoraría.
    val modoSimulacion: StateFlow<ModoSimulacion>
    fun cambiarModoSimulacion(modo: ModoSimulacion)
}

