package com.ventasropa.model;

import com.ventasropa.enums.Talla;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {

    private Integer id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    @NotNull(message = "La talla del producto es obligatoria")
    private Talla talla;

    @NotNull(message = "El precio del producto es obligatorio")
    @Positive(message = "El precio del producto debe ser positivo")
    private Double precio;

    @NotNull(message = "El stock del producto es obligatorio")
    @PositiveOrZero(message = "El stock del producto debe ser positivo o cero")
    private Integer stock;
}