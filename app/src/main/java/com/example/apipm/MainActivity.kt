package com.example.apipm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.apipm.ui.theme.ApipmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApipmTheme {
                PantallaRecordatorios()
            }
        }
    }
}

// Opciones de filtro de la pantalla
enum class Filtro(val etiqueta: String) {
    TODOS("Todos"),
    PENDIENTES("Pendientes"),
    COMPLETADOS("Completados"),
    VENCIDOS("Vencidos"),
    ALTA("Prioridad alta")
}

// Texto legible para cada estado
fun EstadoRecordatorio.texto(): String = when (this) {
    EstadoRecordatorio.Pendiente -> "Pendiente"
    EstadoRecordatorio.Completado -> "Completado"
    is EstadoRecordatorio.Vencido -> "Vencido ($diasDeRetraso días)"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRecordatorios() {
    var filtroActual by remember { mutableStateOf(Filtro.TODOS) }

    val lista = when (filtroActual) {
        Filtro.TODOS -> DatosSimulados.recordatorios
        Filtro.PENDIENTES -> DatosSimulados.pendientes
        Filtro.COMPLETADOS -> DatosSimulados.completados
        Filtro.VENCIDOS -> DatosSimulados.vencidos
        Filtro.ALTA -> DatosSimulados.prioridadAlta
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Mis recordatorios") })
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(Filtro.entries) { filtro ->
                    FilterChip(
                        selected = filtro == filtroActual,
                        onClick = { filtroActual = filtro },
                        label = { Text(filtro.etiqueta) }
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(lista, key = { it.id }) { recordatorio ->
                    TarjetaRecordatorio(recordatorio)
                }
            }
        }
    }
}

@Composable
fun TarjetaRecordatorio(recordatorio: Recordatorio) {
    val colorCategoria = DatosSimulados.coloresPorCategoria[recordatorio.categoria] ?: Color.Gray

    val colorPrioridad = when (recordatorio.prioridad) {
        Prioridad.Alta -> MaterialTheme.colorScheme.error
        Prioridad.Media -> MaterialTheme.colorScheme.tertiary
        Prioridad.Baja -> MaterialTheme.colorScheme.primary
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(colorCategoria, CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recordatorio.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                recordatorio.descripcion?.let {
                    Text(text = it, style = MaterialTheme.typography.bodyMedium)
                }
                Text(
                    text = "Categoría: ${recordatorio.categoria.nombre}",
                    style = MaterialTheme.typography.bodySmall
                )
                recordatorio.ubicacion?.let {
                    Text(text = "Ubicación: $it", style = MaterialTheme.typography.bodySmall)
                }
                Text(
                    text = "Prioridad: ${recordatorio.prioridad.nombre}",
                    style = MaterialTheme.typography.labelLarge,
                    color = colorPrioridad
                )
                Text(
                    text = "Estado: ${recordatorio.estado.texto()}",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaRecordatoriosPreview() {
    ApipmTheme {
        PantallaRecordatorios()
    }
}