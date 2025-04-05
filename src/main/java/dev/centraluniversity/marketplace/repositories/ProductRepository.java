package dev.centraluniversity.marketplace.repositories;

import dev.centraluniversity.marketplace.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(String category);

    List<Product> findByPriceBetween(BigDecimal price, BigDecimal price2);

    List<Product> findByCategoryAndPriceBetween(String category, BigDecimal price, BigDecimal price2);
}