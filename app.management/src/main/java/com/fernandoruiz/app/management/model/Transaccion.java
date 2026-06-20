package com.fernandoruiz.app.management.model;

// model/Transaccion.java



import com.fernandoruiz.app.management.enums.TipoTransaccion;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacciones")
@Data
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransaccion tipoTransaccion;

    private String categoria;

    private LocalDateTime fechaTransaccion;

    @PrePersist
    protected void onCreate() {
        fechaTransaccion = LocalDateTime.now();
    }
}