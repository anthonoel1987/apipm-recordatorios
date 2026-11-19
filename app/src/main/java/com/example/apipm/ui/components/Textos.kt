package com.example.apipm.ui.components

import com.example.apipm.EstadoRecordatorio

// Texto legible para cada estado
fun EstadoRecordatorio.texto(): String = when (this) {
    EstadoRecordatorio.Pendiente -> "Pendiente"
    EstadoRecordatorio.Completado -> "Completado"
    is EstadoRecordatorio.Vencido -> "Vencido ($diasDeRetraso días)"
}

