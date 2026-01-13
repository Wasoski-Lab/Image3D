package com.example.image3d.ui

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.image3d.domain.DetectedObject
import java.io.InputStream
import android.graphics.BitmapFactory
import androidx.compose.foundation.lazy.LazyColumn

@Composable
fun HomeScreen() {
    var selectedImage by remember { mutableStateOf<Bitmap?>(null) }
    var detectedObjects by remember { mutableStateOf<List<DetectedObject>>(emptyList()) }
    var isProcessing by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val inputStream: InputStream? = context.contentResolver.openInputStream(it)
            selectedImage = BitmapFactory.decodeStream(inputStream)
            // Trigger processing here in a real app
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (selectedImage != null) {
            Image(
                bitmap = selectedImage!!.asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier.height(300.dp).fillMaxWidth()
            )
            
            if (isProcessing) {
                CircularProgressIndicator()
                Text("Generating 3D Model...")
            } else {
                Button(onClick = { /* TODO: Launch 3D View */ }) {
                    Text("View in 3D")
                }
                
                // Object Toggles
                LazyColumn {
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
                                   // Update state
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
            Button(onClick = { galleryLauncher.launch("image/*") }) {
                Text("Select Image from Gallery")
            }
            Text("Or take a picture (Camera not implemented in this demo)")
        }
    }
}



