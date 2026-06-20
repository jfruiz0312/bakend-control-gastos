package com.fernandoruiz.app.management.repository;

// repository/ProductoRepository.java

import com.fernandoruiz.app.management.enums.CategoriaProducto;
import com.fernandoruiz.app.management.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findByCodigo(String codigo);

    @Query("SELECT p FROM Producto p WHERE p.stockActual < p.stockMinimo")
    List<Producto> findProductosConStockBajo();

    List<Producto> findByCategoria(CategoriaProducto categoria);
}
