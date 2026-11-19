package com.example.apipm.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics

// Campo de texto controlado: el valor y los cambios vienen de afuera.
// Comunica el error con isError, un texto y una propiedad semántica,
// de modo que el color no es el único indicador.
@Composable
fun CampoTextoValidado(
    valor: String,
    onValorChange: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    mensajeError: String? = null,
    textoAyuda: String? = null,
    enabled: Boolean = true,
    lineas: Int = 1
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(etiqueta) },
        isError = mensajeError != null,
        enabled = enabled,
        singleLine = lineas == 1,
        minLines = lineas,
        maxLines = if (lineas == 1) 1 else lineas + 2,
        supportingText = {
            val texto = mensajeError ?: textoAyuda
            if (texto != null) {
                Text(texto)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                if (mensajeError != null) {
                    error(mensajeError)
                }
            }
    )
}
