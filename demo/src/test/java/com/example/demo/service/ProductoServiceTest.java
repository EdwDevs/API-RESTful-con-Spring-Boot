package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class ProductoServiceTest {

    private ProductoService productoService;
    private Producto productoEjemplo;

    @BeforeEach
    void setUp() {
        productoService = new ProductoService();
        productoEjemplo = new Producto();
        productoEjemplo.setNombre("Producto Test");
        productoEjemplo.setPrecio(100.0);
        productoEjemplo.setStock(10);
    }

    @Test
    void testGuardarProducto() {
        StepVerifier.create(productoService.guardarProducto(productoEjemplo))
                .expectNextMatches(producto ->
                        producto.getNombre().equals("Producto Test") &&
                                producto.getPrecio() == 100.0 &&
                                producto.getStock() == 10 &&
                                producto.getId() != null
                )
                .verifyComplete();
    }

    @Test
    void testObtenerProductos() {
        // Guardar producto primero
        StepVerifier.create(productoService.guardarProducto(productoEjemplo)
                        .then(productoService.obtenerProductos().collectList()))
                .expectNextMatches(productos ->
                        !productos.isEmpty() &&
                                productos.stream().anyMatch(p -> p.getNombre().equals("Producto Test"))
                )
                .verifyComplete();
    }

    @Test
    void testObtenerProductoPorId() {
        // Guardar y obtener el ID
        StepVerifier.create(productoService.guardarProducto(productoEjemplo)
                        .flatMap(producto -> productoService.obtenerProductoPorId(producto.getId())))
                .expectNextMatches(p -> p.getNombre().equals("Producto Test"))
                .verifyComplete();
    }

    @Test
    void testObtenerProductoNoExistente() {
        StepVerifier.create(productoService.obtenerProductoPorId("id-no-existente"))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void testActualizarStock() {
        // Guardar producto y luego actualizar stock
        StepVerifier.create(productoService.guardarProducto(productoEjemplo)
                        .flatMap(producto -> productoService.actualizarStock(producto.getId(), 5)
                                .flatMap(p -> productoService.obtenerProductoPorId(p.getId()))))
                .expectNextMatches(p -> p.getStock() == 5)
                .verifyComplete();
    }
}