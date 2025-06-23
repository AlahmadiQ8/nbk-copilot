package com.example.copilotdemo.services

import com.example.copilotdemo.models.Product
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class ProductsService {

    private val restTemplate = RestTemplate()
    private val baseUrl = "https://dummyjson.com/products"

    data class DummyJsonResponse(
        val products: List<DummyJsonProduct>,
        val total: Int
    )

    data class DummyJsonProduct(
        val id: Int,
        val title: String,
        val description: String,
        val price: Double,
        val brand: String,
        val thumbnail: String
    )

    data class ProductsResult(
        val products: List<Product>,
        val total: Int
    )

    fun getProductsAndTotal(start: Int, size: Int): ProductsResult {
        if (start < 0 || size < 1) return ProductsResult(emptyList(), 0)
        val url = UriComponentsBuilder.fromUriString(baseUrl)
            .queryParam("skip", start)
            .queryParam("limit", size)
            .toUriString()
        val response = restTemplate.getForObject(url, DummyJsonResponse::class.java)
        val products = response?.products?.map {
            Product(
                id = it.id.toLong(),
                title = it.title,
                description = it.description,
                price = it.price,
                brand = it.brand,
                thumbnail = it.thumbnail
            )
        } ?: emptyList()
        val total = response?.total ?: 0
        return ProductsResult(products, total)
    }
}
