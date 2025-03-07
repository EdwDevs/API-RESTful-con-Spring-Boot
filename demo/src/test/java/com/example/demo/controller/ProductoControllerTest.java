package com.example.demo.controller;

import com.example.demo.config.SecurityConfig;
import com.example.demo.model.ApiResponse;
import com.example.demo.model.Producto;
import com.example.demo.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(ProductoController.class)
@Import(SecurityConfig.class) // Importa explícitamente tu configuración de seguridad
class ProductoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private MessageSource messageSource;

    @Test
    void testListarProductos() {
        Producto producto = new Producto();
        producto.setId("1");
        producto.setNombre("Producto Test");
        producto.setPrecio(100.0);
        producto.setStock(10);

        Mockito.when(productoService.obtenerProductos())
                .thenReturn(Flux.just(producto));

        webTestClient.get()
                .uri("/api/productos")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ApiResponse.class)
                .hasSize(1);
    }

    @Test
    void testCrearProducto() {
        Producto producto = new Producto();
        producto.setId("nuevo-id"); // Añadimos un ID para pasar la validación
        producto.setNombre("Nuevo Producto");
        producto.setPrecio(150.0);
        producto.setStock(20);

        Producto productoGuardado = new Producto();
        productoGuardado.setId("nuevo-id");
        productoGuardado.setNombre("Nuevo Producto");
        productoGuardado.setPrecio(150.0);
        productoGuardado.setStock(20);

        Mockito.when(productoService.guardarProducto(Mockito.any(Producto.class)))
                .thenReturn(Mono.just(productoGuardado));

        webTestClient.post()
                .uri("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(producto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data.id").isEqualTo("nuevo-id");
    }
}