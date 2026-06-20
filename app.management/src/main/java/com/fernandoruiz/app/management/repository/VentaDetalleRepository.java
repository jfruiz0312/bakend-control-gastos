package com.fernandoruiz.app.management.repository;

import com.fernandoruiz.app.management.model.VentaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaDetalleRepository extends JpaRepository<VentaDetalle, Long> {
    @Query("SELECT COALESCE(SUM(vd.gananciaTotal), 0) FROM VentaDetalle vd")
    BigDecimal sumGananciaTotal();

    @Query("SELECT COALESCE(SUM(vd.gananciaTotal), 0) FROM VentaDetalle vd WHERE vd.venta.fechaVenta BETWEEN :inicio AND :fin")
    BigDecimal sumGananciaTotalByFecha(@Param("inicio") LocalDateTime inicio,
                                       @Param("fin") LocalDateTime fin);

    @Query("SELECT COALESCE(SUM(vd.cantidad), 0) FROM VentaDetalle vd WHERE vd.venta.fechaVenta BETWEEN :inicio AND :fin")
    Long sumCantidadByFecha(@Param("inicio") LocalDateTime inicio,
                            @Param("fin") LocalDateTime fin);

    @Query("""
            SELECT p.id, p.codigo, p.nombre,
                   COALESCE(SUM(vd.cantidad), 0),
                   COALESCE(SUM(vd.subtotalVenta), 0),
                   COALESCE(SUM(vd.costoRealUnitario * vd.cantidad), 0),
                   COALESCE(SUM(vd.gananciaTotal), 0)
            FROM VentaDetalle vd
            JOIN vd.producto p
            GROUP BY p.id, p.codigo, p.nombre
            ORDER BY COALESCE(SUM(vd.gananciaTotal), 0) DESC
            """)
    List<Object[]> obtenerGananciasPorProducto();
}
