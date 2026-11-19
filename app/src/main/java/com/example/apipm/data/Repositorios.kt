package com.example.apipm.data

// Punto único de acceso al repositorio. Todas las pantallas comparten la
// misma instancia, así lo que se guarda en una se ve en las demás.
object Repositorios {
    val recordatorios: RecordatorioRepositorio by lazy { RecordatorioRepositorioEnMemoria() }
}