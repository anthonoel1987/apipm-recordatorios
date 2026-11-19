package com.example.apipm.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.apipm.DatosSimulados
import com.example.apipm.Prioridad
import com.example.apipm.Recordatorio

// Tarjeta reutilizable: se usa en el listado y puede usarse en otras pantallas.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TarjetaRecordatorio(
    recordatorio: Recordatorio,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorCategoria = DatosSimulados.coloresPorCategoria[recordatorio.categoria] ?: Color.Gray

    val colorPrioridad = when (recordatorio.prioridad) {
        Prioridad.Alta -> MaterialTheme.colorScheme.error
        Prioridad.Media -> MaterialTheme.colorScheme.tertiary
        Prioridad.Baja -> MaterialTheme.colorScheme.primary
    }

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
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
