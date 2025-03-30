package dev.centraluniversity.marketplace.repositories;

import dev.centraluniversity.marketplace.exceptions.ConflictException;
import dev.centraluniversity.marketplace.models.Product;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ProductRepository {

    private final RowMapper<Product> rowMapper = (rs, rowNum) -> new Product(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getDouble("price"),
            rs.getString("category")
    );

    private final JdbcTemplate jdbcTemplate;

    public Product save(Product product) {
        if (product.getId() != null) {
            throw new ConflictException("Product already exists");
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO products (name, description, price, category) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setDouble(3, product.getPrice());
            ps.setString(4, product.getCategory());
            return ps;
        }, keyHolder);


        Number key = (Integer)Objects.requireNonNull(keyHolder.getKeys()).get("id");
        product.setId(key.longValue());
        return product;
    }

    public Optional<Product> findById(Long id) {
        List<Product> products = jdbcTemplate.query(
                "SELECT * FROM products WHERE id = ?",
                rowMapper,
                id);
        return products.stream().findFirst();
    }

    public List<Product> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM products",
                rowMapper);
    }

    public List<Product> findByCategory(String category) {
        return jdbcTemplate.query(
                "SELECT * FROM products WHERE category = ?",
                rowMapper,
                category);
    }

    public List<Product> findByNameContaining(String name) {
        return jdbcTemplate.query(
                "SELECT * FROM products WHERE name LIKE ?",
                rowMapper,
                "%" + name + "%");
    }

    public Product update(Product product) {
        jdbcTemplate.update(
                "UPDATE products SET name = ?, description = ?, price = ?, category = ? WHERE id = ?",
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getId());
        return product;
    }

    public boolean delete(Long id) {
        return jdbcTemplate.update(
                "DELETE FROM products WHERE id = ?",
                id) > 0;
    }
}