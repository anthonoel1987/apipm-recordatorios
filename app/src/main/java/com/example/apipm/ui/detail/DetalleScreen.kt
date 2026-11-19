package com.example.apipm.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apipm.DatosSimulados
import com.example.apipm.Prioridad
import com.example.apipm.Recordatorio
import com.example.apipm.ui.components.PantallaCargando
import com.example.apipm.ui.components.PantallaError
import com.example.apipm.ui.components.PantallaVacia
import com.example.apipm.ui.components.texto

// Versión con estado: obtiene el ViewModel y observa su UiState.
@Composable
fun DetalleScreen(
    id: Int,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetalleViewModel = viewModel(factory = DetalleViewModel.crearFactory(id))
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetalleContenido(
        uiState = uiState,
        onVolver = onVolver,
        onReintentar = viewModel::cargar,
        modifier = modifier
    )
}

// Versión sin estado: solo dibuja lo que indica el UiState.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleContenido(
    uiState: DetalleUiState,
    onVolver: () -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    TextButton(onClick = onVolver) { Text("← Volver") }
                }
            )
        }
    ) { innerPadding ->
        val modificadorContenido = Modifier
            .padding(innerPadding)
            .fillMaxSize()

        when (uiState) {
            DetalleUiState.Cargando -> PantallaCargando(modifier = modificadorContenido)

            DetalleUiState.NoEncontrado -> PantallaVacia(
                titulo = "Recordatorio no encontrado",
                mensaje = "No existe un recordatorio con ese identificador.",
                modifier = modificadorContenido,
                textoAccion = "Volver",
                onAccion = onVolver
            )

            is DetalleUiState.Error -> PantallaError(
                mensaje = uiState.mensaje,
                onReintentar = onReintentar,
                modifier = modificadorContenido
            )

            is DetalleUiState.Exito -> DatosRecordatorio(
                recordatorio = uiState.recordatorio,
                modifier = modificadorContenido
            )
        }
    }
}

@Composable
private fun DatosRecordatorio(
    recordatorio: Recordatorio,
    modifier: Modifier = Modifier
) {
    val colorCategoria = DatosSimulados.coloresPorCategoria[recordatorio.categoria] ?: Color.Gray

    val colorPrioridad = when (recordatorio.prioridad) {
        Prioridad.Alta -> MaterialTheme.colorScheme.error
        Prioridad.Media -> MaterialTheme.colorScheme.tertiary
        Prioridad.Baja -> MaterialTheme.colorScheme.primary
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = recordatorio.titulo,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Dato(etiqueta = "Descripción", valor = recordatorio.descripcion ?: "Sin descripción")

        Column {
            Text(
                text = "Categoría",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(colorCategoria, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = recordatorio.categoria.nombre,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Dato(etiqueta = "Prioridad", valor = recordatorio.prioridad.nombre, colorValor = colorPrioridad)
        Dato(etiqueta = "Estado", valor = recordatorio.estado.texto())
        Dato(etiqueta = "Ubicación", valor = recordatorio.ubicacion ?: "Sin ubicación")
    }
}

// Par etiqueta / valor reutilizado en la pantalla de detalle.
@Composable
private fun Dato(
    etiqueta: String,
    valor: String,
    colorValor: Color = Color.Unspecified
) {
    Column {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge,
            color = colorValor
        )
    }
}

