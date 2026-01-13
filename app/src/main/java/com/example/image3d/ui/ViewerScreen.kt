package com.example.image3d.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun ViewerScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "3D Interaction View\n(Sceneview Rendering Placeholder)",
            color = Color.White,
            fontSize = 20.sp
        )
        // In a real implementation, AndroidView(factory = { context -> ArSceneView(context) }) would go here
    }
}
