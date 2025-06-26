package com.example.copilotdemo.controllers

import com.example.copilotdemo.services.ProductsService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.client.RestTemplate

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@AutoConfigureMockMvc
class ProductsControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var restTemplate: RestTemplate

    @TestConfiguration
    class ProductsServiceTestConfig {
        @Bean
        fun productsService(restTemplate: RestTemplate): ProductsService {
            val service = ProductsService()
            val restTemplateField = ProductsService::class.java.getDeclaredField("restTemplate")
            restTemplateField.isAccessible = true
            restTemplateField.set(service, restTemplate)
            return service
        }
    }

    @Test
    fun `should return products from mocked restTemplate`() {
        val dummyResponse = ProductsService.DummyJsonResponse(
            products = listOf(
                ProductsService.DummyJsonProduct(1, "Test Product", "Desc", 10.0, "Brand", "thumb.jpg")
            ),
            total = 1
        )
        Mockito.`when`(
            restTemplate.getForObject(Mockito.anyString(), Mockito.eq(ProductsService.DummyJsonResponse::class.java))
        ).thenReturn(dummyResponse)

        val result = mockMvc.perform(get("/api/v1/products?start=0&size=1").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.products[0].title").value("Test Product"))
            .andExpect(jsonPath("$.total").value(1))
            .andReturn()
    }

    // write a test case to handle empty response from the service
    @Test
    fun `should return empty products when service returns no products`() {
        val dummyResponse = ProductsService.DummyJsonResponse(products = emptyList(), total = 0)
        Mockito.`when`(
            restTemplate.getForObject(Mockito.anyString(), Mockito.eq(ProductsService.DummyJsonResponse::class.java))
        ).thenReturn(dummyResponse)

        mockMvc.perform(get("/api/v1/products?start=0&size=10").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.products").isEmpty)
            .andExpect(jsonPath("$.total").value(0))
    }
}

