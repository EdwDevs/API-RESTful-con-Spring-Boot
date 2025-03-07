package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Producto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProductoService {

    private final Map<String, Producto> productos = new ConcurrentHashMap<>();

    // Obtener todos los productos
    public Flux<Producto> obtenerProductos() {
        return Flux.fromIterable(productos.values());
    }

    // Obtener un producto por ID
    public Mono<Producto> obtenerProductoPorId(String id) {
        Producto producto = productos.get(id);
        if (producto == null) {
            return Mono.error(new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        }
        return Mono.just(producto);
    }

    // Guardar un nuevo producto
    public Mono<Producto> guardarProducto(Producto producto) {
        if (producto.getId() == null || producto.getId().isEmpty()) {
            producto.setId(UUID.randomUUID().toString());
        }
        productos.put(producto.getId(), producto);
        return Mono.just(producto);
    }

    // Actualizar un producto existente
    public Mono<Producto> actualizarProducto(String id, Producto producto) {
        if (!productos.containsKey(id)) {
            return Mono.error(new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        }
        producto.setId(id);
        productos.put(id, producto);
        return Mono.just(producto);
    }

    // Eliminar un producto
    public Mono<Void> eliminarProducto(String id) {
        if (!productos.containsKey(id)) {
            return Mono.error(new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        }
        productos.remove(id);
        return Mono.empty();
    }

    // Actualizar stock de un producto
    public Mono<Producto> actualizarStock(String id, int cantidad) {
        return obtenerProductoPorId(id)
                .flatMap(producto -> {
                    int nuevoStock = producto.getStock() - cantidad;
                    if (nuevoStock < 0) {
                        return Mono.error(new IllegalArgumentException("Stock insuficiente"));
                    }
                    producto.setStock(nuevoStock);
                    return Mono.just(producto);
                });
    }
    @PostConstruct
    public void inicializarProductos() {
        // Producto 1
        Producto laptop = new Producto();
        laptop.setId("prod-1");
        laptop.setNombre("Laptop Dell XPS 13");
        laptop.setPrecio(1299.99);
        laptop.setDescripcion("Laptop ultradelgada con pantalla 4K");
        laptop.setStock(10);
        productos.put(laptop.getId(), laptop);

        // Producto 2
        Producto smartphone = new Producto();
        smartphone.setId("prod-2");
        smartphone.setNombre("iPhone 14 Pro");
        smartphone.setPrecio(999.99);
        smartphone.setDescripcion("Último modelo con cámara avanzada");
        smartphone.setStock(15);
        productos.put(smartphone.getId(), smartphone);

        // Producto 3
        Producto auriculares = new Producto();
        auriculares.setId("prod-3");
        auriculares.setNombre("AirPods Pro");
        auriculares.setPrecio(249.99);
        auriculares.setDescripcion("Auriculares con cancelación de ruido");
        auriculares.setStock(20);
        productos.put(auriculares.getId(), auriculares);
    }

    // Método adicional que estamos añadiendo
    public Flux<Producto> obtenerTodos() {
        return Flux.fromIterable(productos.values());
    }
}