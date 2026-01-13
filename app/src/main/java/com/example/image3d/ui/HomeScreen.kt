package com.example.image3d.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.image3d.domain.DetectedObject
import java.io.InputStream

@Composable
fun HomeScreen(
    selectedImage: Bitmap?,
    onImageSelected: (Bitmap) -> Unit,
    onResetImage: () -> Unit,
    onView3D: () -> Unit
) {
    // Local state for objects (mocking detection)
    var detectedObjects by remember { mutableStateOf<List<DetectedObject>>(emptyList()) }
    var isProcessing by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Gallery Launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val inputStream: InputStream? = context.contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            if (bitmap != null) {
                onImageSelected(bitmap)
                // Simulate processing
                isProcessing = true
            }
        }
    }

    // Camera Launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            onImageSelected(bitmap)
            // Simulate processing
            isProcessing = true
        }
    }
    
    // Simulate stopping processing after a delay (mock)
    LaunchedEffect(isProcessing) {
        if (isProcessing) {
            kotlinx.coroutines.delay(2000)
            isProcessing = false
            // Add mock objects
            detectedObjects = listOf(
                DetectedObject(1, "Person", 0),
                DetectedObject(2, "Background", 0)
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (selectedImage != null) {
            Box(contentAlignment = Alignment.TopEnd) {
                Image(
                    bitmap = selectedImage.asImageBitmap(),
                    contentDescription = "Selected Image",
                    modifier = Modifier.height(300.dp).fillMaxWidth()
                )
                // Reset Button
                Button(
                    onClick = onResetImage,
                    modifier = Modifier.padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("X")
                }
            }
            
            if (isProcessing) {
                CircularProgressIndicator()
                Text("Generating 3D Model...")
            } else {
                Button(
                    onClick = onView3D,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View in 3D")
                }
                
                Text(
                    "Detected Objects:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Start)
                )
                
                // Object Toggles
                LazyColumn(modifier = Modifier.weight(1f)) {
                   items(detectedObjects.size) { index ->
                       val obj = detectedObjects[index]
                       Row(
                           verticalAlignment = Alignment.CenterVertically,
                           modifier = Modifier.fillMaxWidth()
                       ) {
                           Text(obj.label, modifier = Modifier.weight(1f))
                           Switch(
                               checked = obj.isVisible,
                               onCheckedChange = { isChecked ->
                                   val newList = detectedObjects.toMutableList()
                                   newList[index] = obj.copy(isVisible = isChecked)
                                   detectedObjects = newList
                               }
                           )
                       }
                   }
                }
            }
        } else {
            Spacer(modifier = Modifier.height(50.dp))
            Text("Select an Image to Start", style = MaterialTheme.typography.headlineSmall)
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Button(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pick from Gallery")
            }
            
            Button(
                onClick = { cameraLauncher.launch(null) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Take a Picture")
            }
        }
    }
}
