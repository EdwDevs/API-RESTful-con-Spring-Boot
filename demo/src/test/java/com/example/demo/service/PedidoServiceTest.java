package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.ElementoPedido;
import com.example.demo.model.EstadoPedido;
import com.example.demo.model.Pedido;
import com.example.demo.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class PedidoServiceTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private PedidoService pedidoService;

    private Producto producto;
    private Pedido pedido;
    private ElementoPedido elementoPedido;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configurar producto de prueba
        producto = new Producto();
        producto.setId("prod-1");
        producto.setNombre("Producto Test");
        producto.setPrecio(100.0);
        producto.setStock(10);

        // Configurar elemento de pedido
        elementoPedido = new ElementoPedido();
        elementoPedido.setProductoId("prod-1");
        elementoPedido.setCantidad(2);

        // Configurar pedido
        pedido = new Pedido();
        List<ElementoPedido> elementos = new ArrayList<>();
        elementos.add(elementoPedido);
        pedido.setElementos(elementos);

        // Configurar mock del producto service
        Mockito.when(productoService.obtenerProductoPorId("prod-1"))
                .thenReturn(Mono.just(producto));
        Mockito.when(productoService.guardarProducto(any(Producto.class)))
                .thenReturn(Mono.just(producto));
    }

    @Test
    void testCrearPedido() {
        StepVerifier.create(pedidoService.crearPedido(pedido))
                .expectNextMatches(p ->
                        p.getId() != null &&
                                p.getEstado().equals(EstadoPedido.PENDIENTE.name()) &&
                                p.getElementos().size() == 1 &&
                                p.getElementos().get(0).getNombreProducto().equals("Producto Test") &&
                                p.getElementos().get(0).getPrecioUnitario() == 100.0 &&
                                p.getTotal() == 200.0
                )
                .verifyComplete();
    }

    @Test
    void testCrearPedidoStockInsuficiente() {
        // Modificar elemento para pedir más de lo disponible
        elementoPedido.setCantidad(15); // Stock disponible es 10

        StepVerifier.create(pedidoService.crearPedido(pedido))
                .expectError(BadRequestException.class)
                .verify();
    }

    @Test
    void testObtenerPedidos() {
        // Primero crear un pedido
        StepVerifier.create(pedidoService.crearPedido(pedido)
                        .then(pedidoService.obtenerPedidos().collectList()))
                .expectNextMatches(pedidos ->
                        !pedidos.isEmpty() &&
                                pedidos.get(0).getElementos().get(0).getNombreProducto().equals("Producto Test")
                )
                .verifyComplete();
    }
}