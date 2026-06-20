package com.fernandoruiz.app.management.repository;

import com.fernandoruiz.app.management.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findAllByOrderByFechaVentaDesc();

    @Query("SELECT COALESCE(SUM(v.totalVenta), 0) FROM Venta v WHERE v.fechaVenta BETWEEN :inicio AND :fin")
    BigDecimal sumTotalVentaByFechaVentaBetween(@Param("inicio") LocalDateTime inicio,
                                                @Param("fin") LocalDateTime fin);
}
