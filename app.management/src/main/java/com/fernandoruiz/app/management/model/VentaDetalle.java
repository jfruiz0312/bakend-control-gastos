package com.fernandoruiz.app.management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ventas_detalle")
public class VentaDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal precioVentaUnitario;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal subtotalVenta;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal costoRealUnitario;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal gananciaUnitaria;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal gananciaTotal;
}
