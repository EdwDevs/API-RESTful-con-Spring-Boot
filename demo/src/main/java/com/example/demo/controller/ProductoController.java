package com.example.demo.controller;

import com.example.demo.model.ApiResponse;
import com.example.demo.model.Producto;
import com.example.demo.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public Flux<ApiResponse<Producto>> listarProductos() {
        return productoService.obtenerProductos()
                .map(producto -> ApiResponse.success(producto, "Producto recuperado"));
    }

    @GetMapping("/{id}")
    public Mono<ApiResponse<Producto>> obtenerProducto(@PathVariable String id) {
        return productoService.obtenerProductoPorId(id)
                .map(producto -> ApiResponse.success(producto, "Producto encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ApiResponse<Producto>> crearProducto(@Valid @RequestBody Producto producto) {
        return productoService.guardarProducto(producto)
                .map(p -> ApiResponse.success(p, "Producto creado correctamente"));
    }

    @PutMapping("/{id}")
    public Mono<ApiResponse<Producto>> actualizarProducto(
            @PathVariable String id,
            @Valid @RequestBody Producto producto) {

        producto.setId(id);
        return productoService.guardarProducto(producto)
                .map(p -> ApiResponse.success(p, "Producto actualizado correctamente"));
    }

    @DeleteMapping("/{id}")
    public Mono<ApiResponse<Void>> eliminarProducto(@PathVariable String id) {
        return productoService.eliminarProducto(id)
                .then(Mono.just(ApiResponse.success(null, "Producto eliminado correctamente")));
    }
}