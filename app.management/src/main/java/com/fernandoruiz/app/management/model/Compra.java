package com.fernandoruiz.app.management.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compras", indexes = {
        @Index(name = "idx_compra_fecha", columnList = "fecha_compra"),
        @Index(name = "idx_compra_proveedor", columnList = "proveedor")
})
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String proveedor;

    @Column(name = "fecha_compra", nullable = false)
    private LocalDateTime fechaCompra;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal subtotalCompra;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal totalGastosAsociados;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal totalCompra;

    @Builder.Default
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompraDetalle> detalles = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GastoOperativo> gastosAsociados = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (fechaCompra == null) {
            fechaCompra = LocalDateTime.now();
        }
    }

    public void addDetalle(CompraDetalle detalle) {
        detalles.add(detalle);
        detalle.setCompra(this);
    }

    public void addGasto(GastoOperativo gastoOperativo) {
        gastosAsociados.add(gastoOperativo);
        gastoOperativo.setCompra(this);
    }
}
