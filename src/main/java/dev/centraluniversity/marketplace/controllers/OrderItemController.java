package dev.centraluniversity.marketplace.controllers;

import dev.centraluniversity.marketplace.dto.OrderItemDto;
import dev.centraluniversity.marketplace.models.OrderItem;
import dev.centraluniversity.marketplace.services.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders/{orderId}/items")
@RequiredArgsConstructor
@Tag(name = "Order Items", description = "Order items management")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @Operation(summary = "Find items by order ID", description = "Get all items for a specific order")
    @ApiResponse(responseCode = "200", description = "List of order items")
    @GetMapping
    public List<OrderItem> getOrderItems(@PathVariable Long orderId) {
        return orderItemService.getOrderItemsByOrderId(orderId);
    }

    @Operation(summary = "Find order item by ID", description = "Get order item by its id")
    @ApiResponse(responseCode = "200", description = "Order item found")
    @ApiResponse(responseCode = "404", description = "Order item not found")
    @GetMapping("/{itemId}")
    public ResponseEntity<OrderItem> getOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderItemService.getOrderItemById(itemId)
                .filter(item -> item.getOrderId().equals(orderId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new order item", description = "Adds a new item to an order")
    @ApiResponse(responseCode = "200", description = "Order item created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid order item data")
    @PostMapping
    public ResponseEntity<OrderItem> addOrderItem(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderItemDto orderItemDto) {
        OrderItem createdItem = orderItemService.createOrderItem(orderId, orderItemDto);
        return ResponseEntity.ok(createdItem);
    }

    @PutMapping("/{itemId}")
    @Operation(summary = "Update order item", description = "Updates an existing order item")
    @ApiResponse(responseCode = "200", description = "Order item updated successfully")
    @ApiResponse(responseCode = "404", description = "Order item not found")
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
    @Operation(summary = "Delete order item", description = "Removes an item from an order")
    @ApiResponse(responseCode = "200", description = "Order item deleted successfully")
    @ApiResponse(responseCode = "404", description = "Order item not found")
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
}