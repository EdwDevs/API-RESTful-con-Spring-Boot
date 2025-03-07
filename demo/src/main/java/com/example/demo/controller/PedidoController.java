package com.example.demo.controller;

import com.example.demo.model.ApiResponse;
import com.example.demo.model.Pedido;
import com.example.demo.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.example.demo.exception.ResourceNotFoundException;

import java.util.Locale;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final MessageSource messageSource;

    public PedidoController(PedidoService pedidoService, MessageSource messageSource) {
        this.pedidoService = pedidoService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public Flux<ApiResponse<Pedido>> listarPedidos(
            @RequestParam(defaultValue = "en") String lang) {

        Locale locale = new Locale(lang);
        String mensaje = messageSource.getMessage("pedido.lista", null, locale);

        return pedidoService.obtenerPedidos()
                .map(pedido -> ApiResponse.success(pedido, mensaje + ": " + pedido.getId()));
    }

    @GetMapping("/{id}")
    public Mono<ApiResponse<Pedido>> obtenerPedido(
            @PathVariable String id,
            @RequestParam(defaultValue = "en") String lang) {

        Locale locale = new Locale(lang);

        return pedidoService.obtenerPedidoPorId(id)
                .map(pedido -> {
                    String mensaje = messageSource.getMessage("pedido.detalle", null, locale);
                    return ApiResponse.success(pedido, mensaje + ": " + pedido.getId());
                });
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ApiResponse<Pedido>> crearPedido(
            @Valid @RequestBody Pedido pedido,
            @RequestParam(defaultValue = "en") String lang) {

        Locale locale = new Locale(lang);

        return pedidoService.crearPedido(pedido)
                .map(nuevoPedido -> {
                    String mensaje = messageSource.getMessage("pedido.creado", null, locale);
                    return ApiResponse.success(nuevoPedido, mensaje + ": " + nuevoPedido.getId());
                });
    }
    @DeleteMapping("/{id}")
    public Mono<ApiResponse<Object>> eliminarPedido(
            @PathVariable String id,
            @RequestParam(defaultValue = "en") String lang) {

        Locale locale = new Locale(lang);

        return pedidoService.eliminarPedido(id)
                .then(Mono.fromCallable(() -> {
                    String mensaje = messageSource.getMessage("pedido.eliminado", null, locale);
                    return ApiResponse.success(null, mensaje + ": " + id);
                }))
                .onErrorResume(ResourceNotFoundException.class, ex -> {
                    String mensaje = messageSource.getMessage("pedido.noEncontrado", null, locale);
                    return Mono.just(ApiResponse.error(null, mensaje + ": " + id));
                });
    }
}