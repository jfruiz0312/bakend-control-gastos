package com.fernandoruiz.app.management.repository;

import com.fernandoruiz.app.management.enums.TipoTransaccion;
import com.fernandoruiz.app.management.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    @Query("SELECT SUM(t.monto) FROM Transaccion t WHERE t.tipoTransaccion = :tipo")
    BigDecimal sumByTipo(@Param("tipo") TipoTransaccion tipo);

    List<Transaccion> findByFechaTransaccionBetween(LocalDateTime inicio, LocalDateTime fin);

    List<Transaccion> findByTipoTransaccionOrderByFechaTransaccionDesc(TipoTransaccion tipo);
}
