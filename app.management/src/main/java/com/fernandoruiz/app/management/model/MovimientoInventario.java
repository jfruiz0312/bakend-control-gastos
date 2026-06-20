package com.fernandoruiz.app.management.model;

// model/MovimientoInventario.java

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos_inventario")
@Data
public class MovimientoInventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipo;

    @Column(nullable = false)
    private Integer cantidad;

    private BigDecimal precioUnitario;

    private String observacion;

    private LocalDateTime fechaMovimiento;

    @PrePersist
    protected void onCreate() {
        fechaMovimiento = LocalDateTime.now();
    }

    public enum TipoMovimiento {
        ENTRADA, SALIDA, AJUSTE
    }
}