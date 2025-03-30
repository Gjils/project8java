package dev.centraluniversity.marketplace.services;

import dev.centraluniversity.marketplace.dto.OrderDto;
import dev.centraluniversity.marketplace.dto.OrderItemDto;
import dev.centraluniversity.marketplace.exceptions.NotFoundException;
import dev.centraluniversity.marketplace.models.Order;
import dev.centraluniversity.marketplace.models.OrderItem;
import dev.centraluniversity.marketplace.models.OrderStatus;
import dev.centraluniversity.marketplace.models.Product;
import dev.centraluniversity.marketplace.repositories.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderService orderService;
    private final ProductService productService;

    private final OrderItemRepository orderItemRepository;

    public OrderItem createOrderItem(Long orderId, OrderItemDto orderItemDto) {
        if (orderService.getOrderById(orderId).isEmpty()) {
            throw new NotFoundException("Order not found");
        }
        if (productService.getProductById(orderItemDto.getProductId()).isEmpty()) {
            throw new NotFoundException("Product not found");
        }
        OrderItem orderItem = new OrderItem(
                null,
                orderId,
                orderItemDto.getProductId().longValue(),
                orderItemDto.getQuantity(),
                orderItemDto.getPrice()
        );
        return orderItemRepository.save(orderItem);
    }

    public Optional<OrderItem> getOrderItemById(Long id) {
        return orderItemRepository.findById(id);
    }

    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    public Optional<OrderItem> updateOrderItem(Long id, OrderItemDto orderItemDto) {
        return orderItemRepository.findById(id).map(orderItem -> {
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setPrice(orderItemDto.getPrice());
            return orderItemRepository.update(orderItem);
        });
    }

    public boolean deleteOrderItem(Long id) {
        return orderItemRepository.delete(id);
    }

    public Double calculateOrderTotal(Long orderId) {
        return orderItemRepository.findByOrderId(orderId).stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public Integer countItemsInOrder(Long orderId) {
        return orderItemRepository.findByOrderId(orderId).stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }

    public Order createOrder(Long userId, OrderDto dto) {

        Order order = orderService.createOrder(userId, dto);

        Long orderId = order.getId();

        ArrayList<OrderItem> items = new ArrayList<OrderItem>();
        for (OrderItemDto itemDto : dto.getItems()) {
            OrderItem item = createOrderItem(orderId, itemDto);
        }

        order.setItems(items);

        return order;
    }
}