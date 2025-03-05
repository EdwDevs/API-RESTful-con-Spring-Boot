package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Producto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProductoService {

    // Simular una base de datos con un Map
    private final Map<String, Producto> productos = new ConcurrentHashMap<>();

    public ProductoService() {
        // Añadir algunos productos de ejemplo
        productos.put("1", new Producto("1", "Laptop", 999.99, "Laptop de última generación", 5));
        productos.put("2", new Producto("2", "Smartphone", 599.99, "Smartphone con cámara de alta resolución", 10));
        productos.put("3", new Producto("3", "Auriculares", 99.99, "Auriculares inalámbricos", 20));
        productos.put("4", new Producto("4", "Monitor", 299.99, "Monitor 4K 27 pulgadas", 8));
        productos.put("5", new Producto("5", "Teclado", 49.99, "Teclado mecánico RGB", 15));
    }

    public Flux<Producto> obtenerProductos() {
        return Flux.fromIterable(productos.values());
    }

    public Mono<Producto> obtenerProductoPorId(String id) {
        return Mono.justOrEmpty(productos.get(id))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Producto no encontrado con ID: " + id)));
    }

    public Mono<Producto> guardarProducto(Producto producto) {
        if (producto.getId() == null || producto.getId().trim().isEmpty()) {
            return Mono.error(new BadRequestException("El ID del producto no puede estar vacío"));
        }

        // Validaciones adicionales
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            return Mono.error(new BadRequestException("El nombre del producto es obligatorio"));
        }

        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            return Mono.error(new BadRequestException("El precio debe ser mayor que cero"));
        }

        // Valor por defecto para la descripción si está vacía
        if (producto.getDescripcion() == null || producto.getDescripcion().trim().isEmpty()) {
            producto.setDescripcion("Descripción no disponible");
        }

        // Valor por defecto para el stock si es nulo
        if (producto.getStock() == null) {
            producto.setStock(0);
        }

        productos.put(producto.getId(), producto);
        return Mono.just(producto);
    }

    public Mono<Void> eliminarProducto(String id) {
        return Mono.justOrEmpty(productos.get(id))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Producto no encontrado con ID: " + id)))
                .flatMap(p -> {
                    productos.remove(id);
                    return Mono.empty();
                });
    }
}