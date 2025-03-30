package dev.centraluniversity.marketplace.repositories;

import dev.centraluniversity.marketplace.exceptions.ConflictException;
import dev.centraluniversity.marketplace.models.Order;
import dev.centraluniversity.marketplace.models.OrderStatus;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class OrderRepository {

    private final RowMapper<Order> rowMapper = (rs, rowNum) -> new Order(
            rs.getLong("id"),
            rs.getLong("user_id"),
            rs.getTimestamp("order_date").toLocalDateTime(),
            OrderStatus.valueOf(rs.getString("status")),
            null
    );

    private final JdbcTemplate jdbcTemplate;

    public Order save(Order order) {
        if (order.getId() != null) {
            throw new ConflictException("Order already exists");
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO orders (user_id, order_date, status) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, order.getUserId());
            ps.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
            ps.setString(3, order.getStatus().name());
            return ps;
        }, keyHolder);

        Number key = (Integer)Objects.requireNonNull(keyHolder.getKeys()).get("id");
        order.setId(key.longValue());
        return order;
    }

    public Optional<Order> findById(Long id) {
        List<Order> orders = jdbcTemplate.query(
                "SELECT * FROM orders WHERE id = ?",
                rowMapper,
                id);
        return orders.stream().findFirst();
    }

    public List<Order> findByUserId(Long userId) {
        return jdbcTemplate.query(
                "SELECT * FROM orders WHERE user_id = ?",
                rowMapper,
                userId);
    }

    public List<Order> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM orders",
                rowMapper);
    }

    public Order update(Order order) {
        jdbcTemplate.update(
                "UPDATE orders SET user_id = ?, order_date = ?, status = ? WHERE id = ?",
                order.getUserId(),
                Timestamp.valueOf(order.getOrderDate()),
                order.getStatus().name(),
                order.getId());
        return order;
    }

    public boolean delete(Long id) {
        return jdbcTemplate.update(
                "DELETE FROM orders WHERE id = ?",
                id) > 0;
    }
}