package dev.centraluniversity.marketplace.services;

import dev.centraluniversity.marketplace.dto.OrderDto;
import dev.centraluniversity.marketplace.dto.OrderItemDto;
import dev.centraluniversity.marketplace.exceptions.NotFoundException;
import dev.centraluniversity.marketplace.models.Order;
import dev.centraluniversity.marketplace.models.OrderItem;
import dev.centraluniversity.marketplace.models.OrderStatus;
import dev.centraluniversity.marketplace.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserService userService;

    private final OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order createOrder(Long userId, OrderDto dto) {
        if (userService.getUserById(userId).isEmpty()) {
            throw new NotFoundException("User not found");
        }

        Order order = new Order(
                null,
                userId,
                LocalDateTime.now(),
                OrderStatus.NEW,
                null);

        return orderRepository.save(order);
    }

    public Optional<Order> updateOrderStatus(Long id, OrderStatus status) {
        return orderRepository.findById(id).map(order -> {
            order.setStatus(status);
            return orderRepository.update(order);
        });
    }

    public boolean deleteOrder(Long id) {
        return orderRepository.delete(id);
    }

    public Optional<Order> markAsProcessing(Long id) {
        return updateOrderStatus(id, OrderStatus.PROCESSING);
    }

    public Optional<Order> markAsCompleted(Long id) {
        return updateOrderStatus(id, OrderStatus.COMPLETED);
    }
}