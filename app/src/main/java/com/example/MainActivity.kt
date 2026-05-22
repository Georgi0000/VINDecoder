package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ui.VinApp
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.VinViewModel

class MainActivity : ComponentActivity() {
    // Standard recommended ViewModel initialization
    private val viewModel: VinViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Supports full edge-to-edge drawing, including transparent status and navigation bars
        enableEdgeToEdge()
        
        setContent {
            MyApplicationTheme {
                VinApp(viewModel = viewModel)
            }
        }
    }
}
