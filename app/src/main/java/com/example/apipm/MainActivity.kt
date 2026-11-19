package com.example.apipm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.apipm.navigation.AppNavHost
import com.example.apipm.ui.theme.ApipmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApipmTheme {
                AppNavHost()
            }
        }
    }
}