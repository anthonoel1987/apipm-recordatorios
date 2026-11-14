package com.example.apipm

// Prioridades como sealed class
sealed class Prioridad(val nombre: String) {
    data object Baja : Prioridad("Baja")
    data object Media : Prioridad("Media")
    data object Alta : Prioridad("Alta")
}

// Categoría (por ejemplo: Trabajo, Estudio, Personal)
data class Categoria(
    val id: Int,
    val nombre: String
)

// Estados posibles de un recordatorio
sealed class EstadoRecordatorio {
    data object Pendiente : EstadoRecordatorio()
    data object Completado : EstadoRecordatorio()
    data class Vencido(val diasDeRetraso: Int) : EstadoRecordatorio()
}

// Recordatorio principal
data class Recordatorio(
    val id: Int,
    val titulo: String,
    val descripcion: String? = null,   // opcional
    val categoria: Categoria,
    val prioridad: Prioridad,
    val estado: EstadoRecordatorio = EstadoRecordatorio.Pendiente,
    val ubicacion: String? = null      // opcional
) {
    init {
        require(titulo.isNotBlank()) { "El título no puede estar vacío" }
    }
}
