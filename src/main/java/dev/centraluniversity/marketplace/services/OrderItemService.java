package dev.centraluniversity.marketplace.services;

import dev.centraluniversity.marketplace.dto.OrderItemDto;
import dev.centraluniversity.marketplace.models.OrderItem;
import dev.centraluniversity.marketplace.repositories.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Transactional
    public OrderItem createOrderItem(Long orderId, OrderItemDto orderItemDto) {
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

    public List<OrderItem> getOrderItemsByProductId(Long productId) {
        return orderItemRepository.findByProductId(productId);
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

    public boolean deleteAllItemsForOrder(Long orderId) {
        return orderItemRepository.deleteByOrderId(orderId);
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

    public boolean orderContainsProduct(Long orderId, Long productId) {
        return orderItemRepository.findByOrderId(orderId).stream()
                .anyMatch(item -> item.getProductId().equals(productId));
    }
}