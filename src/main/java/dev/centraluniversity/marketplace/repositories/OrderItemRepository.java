package dev.centraluniversity.marketplace.repositories;

import dev.centraluniversity.marketplace.exceptions.ConflictException;
import dev.centraluniversity.marketplace.models.OrderItem;
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
public class OrderItemRepository {

    private final RowMapper<OrderItem> rowMapper = (rs, rowNum) -> new OrderItem(
            rs.getLong("id"),
            rs.getLong("order_id"),
            rs.getLong("product_id"),
            rs.getInt("quantity"),
            rs.getDouble("price")
    );

    private final JdbcTemplate jdbcTemplate;

    public OrderItem save(OrderItem orderItem) {
        if (orderItem.getId() != null) {
            throw new ConflictException("OrderItem already exists");
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, orderItem.getOrderId());
            ps.setLong(2, orderItem.getProductId());
            ps.setInt(3, orderItem.getQuantity());
            ps.setDouble(4, orderItem.getPrice());
            return ps;
        }, keyHolder);

        Number key = (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        orderItem.setId(key.longValue());
        return orderItem;
    }

    public Optional<OrderItem> findById(Long id) {
        List<OrderItem> items = jdbcTemplate.query(
                "SELECT * FROM order_items WHERE id = ?",
                rowMapper,
                id);
        return items.stream().findFirst();
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        return jdbcTemplate.query(
                "SELECT * FROM order_items WHERE order_id = ?",
                rowMapper,
                orderId);
    }

    public List<OrderItem> findByProductId(Long productId) {
        return jdbcTemplate.query(
                "SELECT * FROM order_items WHERE product_id = ?",
                rowMapper,
                productId);
    }

    public OrderItem update(OrderItem orderItem) {
        jdbcTemplate.update(
                "UPDATE order_items SET product_id = ?, quantity = ?, price = ? WHERE id = ?",
                orderItem.getProductId(),
                orderItem.getQuantity(),
                orderItem.getPrice(),
                orderItem.getId());
        return orderItem;
    }

    public boolean delete(Long id) {
        return jdbcTemplate.update(
                "DELETE FROM order_items WHERE id = ?",
                id) > 0;
    }

    public boolean deleteByOrderId(Long orderId) {
        return jdbcTemplate.update(
                "DELETE FROM order_items WHERE order_id = ?",
                orderId) > 0;
    }
}