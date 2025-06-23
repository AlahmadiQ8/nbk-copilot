package com.example.copilotdemo.controllers

import com.example.copilotdemo.models.Product
import com.example.copilotdemo.services.ProductsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/products")
class ProductsController(private val productsService: ProductsService) {
    @GetMapping
    fun getProducts(
        @RequestParam(defaultValue = "0") start: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ProductPageResponse {
        val result = productsService.getProductsAndTotal(start, size)
        return ProductPageResponse(result.products, result.total)
    }
}

data class ProductPageResponse(
    val products: List<Product>,
    val total: Int
)
