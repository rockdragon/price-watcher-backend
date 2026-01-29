package me.moye.hew.database;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import me.moye.hew.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByAsin(String asin);

    Page<Product> findByNameContaining(String keyword, Pageable pageable);

    Product getProductById(long id);

    List<Product> findByIsMonitoredTrue();
}
