package com.example.demo.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                   // Genera getters, setters, equals, hashCode y toString
@AllArgsConstructor     // Genera constructor con todos los argumentos
@NoArgsConstructor      // Genera constructor sin argumentos
public class Producto {

    @NotBlank(message = "El ID no puede estar vacío")
    private String id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El precio no puede ser nulo")
    @Positive(message = "El precio debe ser mayor que cero")
    private Double precio;

    private String descripcion;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    // Constructor adicional para simplificar
    public Producto(String id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = "Descripción por defecto para " + nombre;
        this.stock = 10;
    }
}