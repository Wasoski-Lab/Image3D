package com.example.image3d.domain

data class DetectedObject(
    val id: Int,
    val label: String,
    val color: Int, // Color mask value or similar ID
    var isVisible: Boolean = true
)
