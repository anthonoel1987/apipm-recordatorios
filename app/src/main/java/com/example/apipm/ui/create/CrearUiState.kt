package com.example.apipm.ui.create

import com.example.apipm.Categoria
import com.example.apipm.Prioridad

// Campos del formulario que pueden mostrar un mensaje de validación.
enum class CampoFormulario { TITULO, DESCRIPCION, CATEGORIA, PRIORIDAD }

// Estado inmutable de la pantalla de creación.
data class CrearUiState(
    // Valores actuales de los campos
    val titulo: String = "",
    val descripcion: String = "",
    val categoria: Categoria? = null,
    val prioridad: Prioridad? = null,
    // Opciones que ofrece el formulario
    val opcionesCategoria: List<Categoria> = emptyList(),
    val opcionesPrioridad: List<Prioridad> = emptyList(),
    // Validación: los mensajes se muestran desde la primera interacción
    val validacionVisible: Boolean = false,
    val errores: Map<CampoFormulario, String> = emptyMap(),
    val formularioValido: Boolean = false,
    // Envío y resultado
    val enviando: Boolean = false,
    val guardadoExitoso: Boolean = false,
    val errorGuardado: String? = null
) {
    // Guardar solo se habilita con formulario válido y sin envío en curso.
    val puedeGuardar: Boolean
        get() = formularioValido && !enviando
}