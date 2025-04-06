package dev.centraluniversity.marketplace.services;

import dev.centraluniversity.marketplace.models.Order;
import dev.centraluniversity.marketplace.models.OrderItem;
import dev.centraluniversity.marketplace.models.OrderStatus;
import dev.centraluniversity.marketplace.repositories.OrderItemRepository;
import dev.centraluniversity.marketplace.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository itemRepository;

    public Order createOrder(Order order, List<OrderItem> items) {
        Order savedOrder = orderRepository.save(order);
        items.forEach(item -> item.setOrder(savedOrder));
        itemRepository.saveAll(items);
        return savedOrder;
    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElseThrow();
    }

    public Order updateStatus(Long orderId, OrderStatus status) {
        Order order = getOrder(orderId);
        order.setStatus(status);
        order = orderRepository.save(order);
        return order;
    }
}