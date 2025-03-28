package dev.centraluniversity.marketplace.repositories;

import dev.centraluniversity.marketplace.models.User;
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
public class UserRepository {

    private final RowMapper<User> rowMapper = (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("address"),
            rs.getString("phone"),
            null
    );

    private final JdbcTemplate jdbcTemplate;

    public User save(User user) {
        if (user.getId() != null) {
            throw new RuntimeException("User already exists");
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO users (name, email, address, phone) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getAddress());
            ps.setString(4, user.getPhone());
            return ps;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;

    }

    public Optional<User> findById(Long id) {
        List<User> users = jdbcTemplate.query(
                "SELECT name, email, phone, address FROM users WHERE id = ?",
                rowMapper,
                id);
        return users.stream().findFirst();
    }

    public Optional<User> findByEmail(String email) {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM users WHERE email = ?",
                rowMapper,
                email);
        return users.stream().findFirst();
    }

    public List<User> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users",
                rowMapper);
    }

    public User update(User user) {
        jdbcTemplate.update(
                "UPDATE users SET name = ?, email = ?, address = ?, phone = ? WHERE id = ?",
                user.getName(),
                user.getEmail(),
                user.getAddress(),
                user.getPhone(),
                user.getId());
        return user;
    }

    public boolean delete(Long id) {
        return jdbcTemplate.update(
                "DELETE FROM users WHERE id = ?",
                id) > 0;
    }
}
