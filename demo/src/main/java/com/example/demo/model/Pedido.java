package com.example.demo.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    private String id;

    @NotBlank(message = "El ID del cliente es obligatorio")
    private String clienteId;

    @NotEmpty(message = "El pedido debe contener al menos un producto")
    @Valid
    private List<ElementoPedido> elementos = new ArrayList<>();

    private LocalDateTime fechaPedido;
    private String estado;
    private Double total;

    // Método para calcular el total
    public void calcularTotal() {
        this.total = elementos.stream()
                .mapToDouble(item -> item.getPrecioUnitario() * item.getCantidad())
                .sum();
    }
}