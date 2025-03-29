package dev.centraluniversity.marketplace.controllers;

import dev.centraluniversity.marketplace.dto.OrderDto;
import dev.centraluniversity.marketplace.models.Order;
import dev.centraluniversity.marketplace.models.OrderStatus;
import dev.centraluniversity.marketplace.services.OrderItemService;
import dev.centraluniversity.marketplace.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management")
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @Operation(summary = "Find orders by user ID", description = "Get all orders for a specific user")
    @ApiResponse(responseCode = "200", description = "List of user orders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @Operation(summary = "Find order by ID", description = "Find order by id")
    @ApiResponse(responseCode = "200", description = "Order found")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "Update order status", description = "Updates an existing order status")
    @ApiResponse(responseCode = "200", description = "Order updated successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam @Valid OrderStatus status) {
        return orderService.updateOrderStatus(id, status)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete order", description = "Removes an order from the system")
    @ApiResponse(responseCode = "200", description = "Order deleted successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrder(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Calculate order total", description = "Calculates the total sum of all items in the order")
    @ApiResponse(responseCode = "200", description = "Order total calculated")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @GetMapping("/{id}/total")
    public ResponseEntity<Double> getOrderTotal(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderItemService.calculateOrderTotal(orderId));
    }

    @Operation(summary = "Calculate items count", description = "Calculates the total number of all items in the order")
    @ApiResponse(responseCode = "200", description = "Items count calculated")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @GetMapping("/{id}/count")
    public ResponseEntity<Integer> getItemCount(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderItemService.countItemsInOrder(orderId));
    }
}