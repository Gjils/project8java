package dev.centraluniversity.marketplace.controllers;

import dev.centraluniversity.marketplace.dto.OrderItemDto;
import dev.centraluniversity.marketplace.models.OrderItem;
import dev.centraluniversity.marketplace.services.OrderItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders/{orderId}/items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping
    public List<OrderItem> getOrderItems(@PathVariable Long orderId) {
        return orderItemService.getOrderItemsByOrderId(orderId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<OrderItem> getOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderItemService.getOrderItemById(itemId)
                .filter(item -> item.getOrderId().equals(orderId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrderItem> addOrderItem(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderItemDto orderItemDto) {
        OrderItem createdItem = orderItemService.createOrderItem(orderId, orderItemDto);
        return ResponseEntity.ok(createdItem);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<OrderItem> updateOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            @RequestBody @Valid OrderItemDto orderItemDto) {
        return orderItemService.getOrderItemById(itemId)
                .filter(item -> item.getOrderId().equals(orderId))
                .flatMap(item -> orderItemService.updateOrderItem(itemId, orderItemDto))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderItemService.getOrderItemById(itemId)
                .filter(item -> item.getOrderId().equals(orderId))
                .map(item -> {
                    orderItemService.deleteOrderItem(itemId);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getOrderTotal(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderItemService.calculateOrderTotal(orderId));
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getItemCount(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderItemService.countItemsInOrder(orderId));
    }
}