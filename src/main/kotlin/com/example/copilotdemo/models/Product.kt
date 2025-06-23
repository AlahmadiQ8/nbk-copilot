package com.example.copilotdemo.models

/**
 * Data class representing a product.
 */
data class Product(
    val id: Long,
    val title: String,
    val description: String,
    val price: Double,
    val brand: String,
    val thumbnail: String
)
