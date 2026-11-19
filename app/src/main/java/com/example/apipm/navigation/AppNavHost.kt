package com.example.apipm.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.apipm.ui.create.CrearScreen
import com.example.apipm.ui.detail.DetalleScreen
import com.example.apipm.ui.list.ListaScreen

// Grafo de navegación de la aplicación.
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Rutas.LISTA,
        modifier = modifier
    ) {
        composable(route = Rutas.LISTA) {
            ListaScreen(
                onCrearRecordatorio = {
                    navController.navigate(Rutas.CREAR) { launchSingleTop = true }
                },
                onAbrirDetalle = { id ->
                    navController.navigate(Rutas.detalle(id)) { launchSingleTop = true }
                }
            )
        }

        composable(route = Rutas.CREAR) {
            // Guardar o cancelar regresan al destino anterior (el listado)
            // sin agregar una nueva copia del listado al historial.
            CrearScreen(
                onGuardado = { navController.popBackStack() },
                onCancelar = { navController.popBackStack() }
            )
        }

        composable(
            route = Rutas.DETALLE,
            arguments = listOf(
                navArgument(Rutas.ARG_ID) { type = NavType.IntType }
            )
        ) { entrada ->
            val id = entrada.arguments?.getInt(Rutas.ARG_ID) ?: -1
            DetalleScreen(
                id = id,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}