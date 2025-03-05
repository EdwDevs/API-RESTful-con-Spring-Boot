package com.example.demo.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElementoPedido {
    @NotBlank(message = "El ID del producto es obligatorio")
    private String productoId;

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    private Double precioUnitario;
    private String nombreProducto;
}