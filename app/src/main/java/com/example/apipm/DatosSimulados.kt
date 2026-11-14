package com.example.apipm

import androidx.compose.ui.graphics.Color

object DatosSimulados {

    private val trabajo = Categoria(1, "Trabajo")
    private val estudio = Categoria(2, "Estudio")
    private val personal = Categoria(3, "Personal")
    private val salud = Categoria(4, "Salud")

    // SET: no admite elementos repetidos. "estudio" se incluye dos veces
    // a propósito para demostrar que el Set la guarda una sola vez.
    val categorias: Set<Categoria> = setOf(trabajo, estudio, personal, salud, estudio)

    // MAP: asocia cada categoría con un color
    val coloresPorCategoria: Map<Categoria, Color> = mapOf(
        trabajo to Color(0xFF1E88E5),
        estudio to Color(0xFF8E24AA),
        personal to Color(0xFF43A047),
        salud to Color(0xFFE53935)
    )

    // LIST: recordatorios simulados
    val recordatorios: List<Recordatorio> = listOf(
        Recordatorio(
            id = 1,
            titulo = "Entregar informe mensual",
            descripcion = "Enviar el informe al jefe de área",
            categoria = trabajo,
            prioridad = Prioridad.Alta,
            estado = EstadoRecordatorio.Vencido(diasDeRetraso = 2),
            ubicacion = "Oficina central"
        ),
        Recordatorio(
            id = 2,
            titulo = "Estudiar para la prueba de Kotlin",
            categoria = estudio,
            prioridad = Prioridad.Alta
        ),
        Recordatorio(
            id = 3,
            titulo = "Comprar útiles",
            descripcion = "Cuadernos y lápices",
            categoria = personal,
            prioridad = Prioridad.Baja,
            estado = EstadoRecordatorio.Completado,
            ubicacion = "Librería del centro"
        ),
        Recordatorio(
            id = 4,
            titulo = "Control médico",
            descripcion = "Llevar exámenes anteriores",
            categoria = salud,
            prioridad = Prioridad.Media,
            ubicacion = "Cesfam"
        ),
        Recordatorio(
            id = 5,
            titulo = "Reunión de equipo",
            categoria = trabajo,
            prioridad = Prioridad.Media,
            estado = EstadoRecordatorio.Completado
        ),
        Recordatorio(
            id = 6,
            titulo = "Pagar cuentas del mes",
            categoria = personal,
            prioridad = Prioridad.Alta,
            estado = EstadoRecordatorio.Vencido(diasDeRetraso = 5)
        ),
        Recordatorio(
            id = 7,
            titulo = "Leer apuntes de programación móvil",
            descripcion = "Capítulo sobre Jetpack Compose",
            categoria = estudio,
            prioridad = Prioridad.Baja
        )
    )

    // Filtros por estado y prioridad
    val pendientes: List<Recordatorio>
        get() = recordatorios.filter { it.estado is EstadoRecordatorio.Pendiente }

    val completados: List<Recordatorio>
        get() = recordatorios.filter { it.estado is EstadoRecordatorio.Completado }

    val vencidos: List<Recordatorio>
        get() = recordatorios.filter { it.estado is EstadoRecordatorio.Vencido }

    val prioridadAlta: List<Recordatorio>
        get() = recordatorios.filter { it.prioridad == Prioridad.Alta }
}