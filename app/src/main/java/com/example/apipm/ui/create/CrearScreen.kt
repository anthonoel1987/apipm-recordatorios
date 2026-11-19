package com.example.apipm.ui.create

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apipm.Categoria
import com.example.apipm.Prioridad
import com.example.apipm.ui.components.CampoTextoValidado
import com.example.apipm.ui.components.SelectorOpciones

// Versión con estado: obtiene el ViewModel, observa su UiState y avisa
// cuando el guardado terminó con éxito.
@Composable
fun CrearScreen(
    onGuardado: () -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CrearViewModel = viewModel(factory = CrearViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Al guardar con éxito, se regresa a la pantalla anterior.
    LaunchedEffect(uiState.guardadoExitoso) {
        if (uiState.guardadoExitoso) {
            onGuardado()
        }
    }

    CrearContenido(
        uiState = uiState,
        onTituloChange = viewModel::onTituloChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        onCategoriaSeleccionada = viewModel::onCategoriaSeleccionada,
        onPrioridadSeleccionada = viewModel::onPrioridadSeleccionada,
        onGuardar = viewModel::guardar,
        onReintentar = viewModel::guardar,
        onCancelar = onCancelar,
        modifier = modifier
    )
}

// Versión sin estado: solo dibuja el UiState y comunica acciones con lambdas.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearContenido(
    uiState: CrearUiState,
    onTituloChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onCategoriaSeleccionada: (Categoria) -> Unit,
    onPrioridadSeleccionada: (Prioridad) -> Unit,
    onGuardar: () -> Unit,
    onReintentar: () -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Mientras se guarda, el botón atrás del sistema no hace nada.
    BackHandler(enabled = uiState.enviando) { }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Nuevo recordatorio") },
                navigationIcon = {
                    TextButton(onClick = onCancelar, enabled = !uiState.enviando) {
                        Text("← Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CampoTextoValidado(
                valor = uiState.titulo,
                onValorChange = onTituloChange,
                etiqueta = "Título",
                mensajeError = uiState.errores[CampoFormulario.TITULO],
                enabled = !uiState.enviando
            )

            CampoTextoValidado(
                valor = uiState.descripcion,
                onValorChange = onDescripcionChange,
                etiqueta = "Descripción (opcional)",
                mensajeError = uiState.errores[CampoFormulario.DESCRIPCION],
                textoAyuda = "${uiState.descripcion.length}/${CrearViewModel.DESCRIPCION_MAXIMA}",
                enabled = !uiState.enviando,
                lineas = 3
            )

            SelectorOpciones(
                titulo = "Categoría",
                opciones = uiState.opcionesCategoria,
                seleccion = uiState.categoria,
                etiqueta = { it.nombre },
                onSeleccionar = onCategoriaSeleccionada,
                mensajeError = uiState.errores[CampoFormulario.CATEGORIA],
                enabled = !uiState.enviando
            )

            SelectorOpciones(
                titulo = "Prioridad",
                opciones = uiState.opcionesPrioridad,
                seleccion = uiState.prioridad,
                etiqueta = { it.nombre },
                onSeleccionar = onPrioridadSeleccionada,
                mensajeError = uiState.errores[CampoFormulario.PRIORIDAD],
                enabled = !uiState.enviando
            )

            uiState.errorGuardado?.let { mensaje ->
                MensajeErrorGuardado(mensaje = mensaje, onReintentar = onReintentar)
            }

            if (uiState.enviando) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Text("Guardando recordatorio...")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCancelar,
                    enabled = !uiState.enviando,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }
                Button(
                    onClick = onGuardar,
                    enabled = uiState.puedeGuardar,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}

// Mensaje de error general de guardado, con opción de reintento.
@Composable
private fun MensajeErrorGuardado(
    mensaje: String,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .semantics { liveRegion = LiveRegionMode.Polite },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "No se pudo guardar",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = mensaje,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            TextButton(onClick = onReintentar) { Text("Reintentar") }
        }
    }
}