package com.example.apipm.ui.list

import com.example.apipm.EstadoRecordatorio
import com.example.apipm.Prioridad
import com.example.apipm.Recordatorio

// Opciones de filtro de la pantalla de listado
enum class Filtro(val etiqueta: String) {
    TODOS("Todos"),
    PENDIENTES("Pendientes"),
    COMPLETADOS("Completados"),
    VENCIDOS("Vencidos"),
    ALTA("Prioridad alta");

    fun aplicar(lista: List<Recordatorio>): List<Recordatorio> = when (this) {
        TODOS -> lista
        PENDIENTES -> lista.filter { it.estado is EstadoRecordatorio.Pendiente }
        COMPLETADOS -> lista.filter { it.estado is EstadoRecordatorio.Completado }
        VENCIDOS -> lista.filter { it.estado is EstadoRecordatorio.Vencido }
        ALTA -> lista.filter { it.prioridad is Prioridad.Alta }
    }
}
