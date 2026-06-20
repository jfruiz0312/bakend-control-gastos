package com.fernandoruiz.app.management.model;


import com.fernandoruiz.app.management.enums.CategoriaProducto;
import com.fernandoruiz.app.management.enums.Color;
import com.fernandoruiz.app.management.enums.Talla;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
// model/Producto.java


@Entity
@Table(name = "productos")
@Data
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaProducto categoria;

    @Enumerated(EnumType.STRING)
    private Talla talla;

    @Enumerated(EnumType.STRING)
    private Color color;

    @Column(nullable = false)
    private Integer stockActual;

    @Column(nullable = false)
    private Integer stockMinimo;

    @Column(nullable = false)
    private BigDecimal precioCompra;

    @Column(nullable = false)
    private BigDecimal precioVenta;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean activo;

    private String proveedor;
    private String temporada; // Verano, Invierno, Primavera, Otoño

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (activo == null) {
            activo = Boolean.TRUE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

}