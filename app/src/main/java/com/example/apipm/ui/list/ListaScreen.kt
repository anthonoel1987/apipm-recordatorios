package com.example.apipm.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apipm.DatosSimulados
import com.example.apipm.data.ModoSimulacion
import com.example.apipm.ui.components.PantallaCargando
import com.example.apipm.ui.components.PantallaError
import com.example.apipm.ui.components.PantallaVacia
import com.example.apipm.ui.components.TarjetaRecordatorio
import com.example.apipm.ui.theme.ApipmTheme

// Versión con estado: obtiene el ViewModel y observa su UiState.
@Composable
fun ListaScreen(
    onCrearRecordatorio: () -> Unit,
    onAbrirDetalle: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListaViewModel = viewModel(factory = ListaViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val modoSimulacion by viewModel.modoSimulacion.collectAsStateWithLifecycle()

    ListaContenido(
        uiState = uiState,
        modoSimulacion = modoSimulacion,
        onFiltroSeleccionado = viewModel::seleccionarFiltro,
        onReintentar = viewModel::reintentar,
        onCambiarModo = viewModel::cambiarModoSimulacion,
        onCrearRecordatorio = onCrearRecordatorio,
        onAbrirDetalle = onAbrirDetalle,
        modifier = modifier
    )
}

// Versión sin estado: solo dibuja lo que le indica el UiState.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaContenido(
    uiState: ListaUiState,
    modoSimulacion: ModoSimulacion,
    onFiltroSeleccionado: (Filtro) -> Unit,
    onReintentar: () -> Unit,
    onCambiarModo: (ModoSimulacion) -> Unit,
    onCrearRecordatorio: () -> Unit,
    onAbrirDetalle: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var menuAbierto by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis recordatorios") },
                actions = {
                    // Menú para simular los estados de la consigna 3
                    Box {
                        TextButton(onClick = { menuAbierto = true }) { Text("Simular") }
                        DropdownMenu(
                            expanded = menuAbierto,
                            onDismissRequest = { menuAbierto = false }
                        ) {
                            ModoSimulacion.entries.forEach { modo ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            if (modo == modoSimulacion) "✓ ${modo.etiqueta}"
                                            else modo.etiqueta
                                        )
                                    },
                                    onClick = {
                                        menuAbierto = false
                                        onCambiarModo(modo)
                                    }
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = onCrearRecordatorio) {
                Text("Nuevo recordatorio")
            }
        }
    ) { innerPadding ->
        val modificadorContenido = Modifier
            .padding(innerPadding)
            .fillMaxSize()

        when (uiState) {
            ListaUiState.Cargando -> PantallaCargando(modifier = modificadorContenido)

            ListaUiState.Vacio -> PantallaVacia(
                titulo = "Aún no tienes recordatorios",
                mensaje = "Crea tu primer recordatorio para verlo aquí.",
                modifier = modificadorContenido,
                textoAccion = "Crear recordatorio",
                onAccion = onCrearRecordatorio
            )

            is ListaUiState.Error -> PantallaError(
                mensaje = uiState.mensaje,
                onReintentar = onReintentar,
                modifier = modificadorContenido
            )

            is ListaUiState.Contenido -> ListaRecordatorios(
                estado = uiState,
                onFiltroSeleccionado = onFiltroSeleccionado,
                onAbrirDetalle = onAbrirDetalle,
                modifier = modificadorContenido
            )
        }
    }
}

@Composable
private fun ListaRecordatorios(
    estado: ListaUiState.Contenido,
    onFiltroSeleccionado: (Filtro) -> Unit,
    onAbrirDetalle: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(Filtro.entries) { filtro ->
                FilterChip(
                    selected = filtro == estado.filtro,
                    onClick = { onFiltroSeleccionado(filtro) },
                    label = { Text(filtro.etiqueta) }
                )
            }
        }

        if (estado.recordatorios.isEmpty()) {
            PantallaVacia(
                titulo = "Sin resultados",
                mensaje = "No hay recordatorios para el filtro «${estado.filtro.etiqueta}»."
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(estado.recordatorios, key = { it.id }) { recordatorio ->
                    TarjetaRecordatorio(
                        recordatorio = recordatorio,
                        onClick = { onAbrirDetalle(recordatorio.id) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListaContenidoPreview() {
    ApipmTheme {
        ListaContenido(
            uiState = ListaUiState.Contenido(Filtro.TODOS, DatosSimulados.recordatorios),
            modoSimulacion = ModoSimulacion.NORMAL,
            onFiltroSeleccionado = {},
            onReintentar = {},
            onCambiarModo = {},
            onCrearRecordatorio = {},
            onAbrirDetalle = {}
        )
    }
}
