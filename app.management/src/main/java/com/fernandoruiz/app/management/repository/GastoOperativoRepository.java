package com.fernandoruiz.app.management.repository;

import com.fernandoruiz.app.management.model.GastoOperativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GastoOperativoRepository extends JpaRepository<GastoOperativo, Long> {
    List<GastoOperativo> findAllByOrderByFechaDesc();

    @Query("SELECT COALESCE(SUM(g.monto), 0) FROM GastoOperativo g WHERE g.fecha BETWEEN :inicio AND :fin")
    BigDecimal sumMontoByFechaBetween(@Param("inicio") LocalDateTime inicio,
                                      @Param("fin") LocalDateTime fin);
}
