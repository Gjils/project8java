package dev.centraluniversity.marketplace.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private List<OrderItem> items;
}