package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ElementoPedido;
import com.example.demo.model.EstadoPedido;
import com.example.demo.model.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PedidoService {

    private final Map<String, Pedido> pedidos = new ConcurrentHashMap<>();

    @Autowired
    private ProductoService productoService;

    public Flux<Pedido> obtenerPedidos() {
        return Flux.fromIterable(pedidos.values());
    }

    public Mono<Pedido> obtenerPedidoPorId(String id) {
        return Mono.justOrEmpty(pedidos.get(id))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Pedido no encontrado con ID: " + id)));
    }

    public Mono<Pedido> crearPedido(Pedido pedido) {
        // Generar ID único si no se proporciona
        if (pedido.getId() == null || pedido.getId().trim().isEmpty()) {
            pedido.setId(UUID.randomUUID().toString());
        }

        // Establecer fecha actual si no se proporciona
        if (pedido.getFechaPedido() == null) {
            pedido.setFechaPedido(LocalDateTime.now());
        }

        // Establecer estado inicial
        pedido.setEstado(EstadoPedido.PENDIENTE.name());

        // Verificar productos y completar información
        return Flux.fromIterable(pedido.getElementos())
                .flatMap(elemento -> {
                    return productoService.obtenerProductoPorId(elemento.getProductoId())
                            .map(producto -> {
                                // Verificar stock
                                if (producto.getStock() < elemento.getCantidad()) {
                                    throw new BadRequestException(
                                            "Stock insuficiente para el producto: " + producto.getNombre());
                                }

                                // Completar información del elemento
                                elemento.setPrecioUnitario(producto.getPrecio());
                                elemento.setNombreProducto(producto.getNombre());

                                // Actualizar stock
                                producto.setStock(producto.getStock() - elemento.getCantidad());
                                productoService.guardarProducto(producto).subscribe();

                                return elemento;
                            });
                })
                .collectList()
                .map(elementos -> {
                    pedido.setElementos(elementos);
                    pedido.calcularTotal();
                    pedidos.put(pedido.getId(), pedido);
                    return pedido;
                });
    }

    public Flux<Pedido> obtenerTodosPedidos() {
        return Flux.fromIterable(pedidos.values());
    }

    // Añadir este método a PedidoService.java (DENTRO de la clase)
    public Mono<Void> eliminarPedido(String id) {
        return Mono.justOrEmpty(pedidos.get(id))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Pedido no encontrado con ID: " + id)))
                .flatMap(pedido -> {
                    // Devolver el stock de los productos
                    return Flux.fromIterable(pedido.getElementos())
                            .flatMap(elemento -> {
                                return productoService.obtenerProductoPorId(elemento.getProductoId())
                                        .flatMap(producto -> {
                                            // Restaurar el stock
                                            producto.setStock(producto.getStock() + elemento.getCantidad());
                                            return productoService.guardarProducto(producto);
                                        });
                            })
                            .then(Mono.fromRunnable(() -> pedidos.remove(id)));
                });
    }
}