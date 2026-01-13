package com.example.image3d

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.image3d.ui.HomeScreen
import com.example.image3d.ui.ViewerScreen

enum class Screen {
    Home, Viewer
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf(Screen.Home) }
                    var selectedImage by remember { mutableStateOf<Bitmap?>(null) }

                    when (currentScreen) {
                        Screen.Home -> {
                            HomeScreen(
                                selectedImage = selectedImage,
                                onImageSelected = { bitmap ->
                                    selectedImage = bitmap
                                },
                                onResetImage = {
                                    selectedImage = null
                                },
                                onView3D = {
                                    if (selectedImage != null) {
                                        currentScreen = Screen.Viewer
                                    }
                                }
                            )
                        }
                        Screen.Viewer -> {
                            ViewerScreen(
                                image = selectedImage,
                                onBack = {
                                    currentScreen = Screen.Home
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
