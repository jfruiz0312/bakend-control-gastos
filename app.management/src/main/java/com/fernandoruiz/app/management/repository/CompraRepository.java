package com.fernandoruiz.app.management.repository;

import com.fernandoruiz.app.management.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findAllByOrderByFechaCompraDesc();
}
