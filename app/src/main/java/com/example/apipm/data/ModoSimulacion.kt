package com.example.apipm.data

// Modos que permiten comprobar todos los estados de la interfaz
// sin depender de una base de datos ni de un servicio remoto.
enum class ModoSimulacion(val etiqueta: String) {
    NORMAL("Datos normales"),
    VACIO("Simular lista vacía"),
    ERROR_CARGA("Simular error de carga"),
    ERROR_GUARDADO("Simular error al guardar")
}