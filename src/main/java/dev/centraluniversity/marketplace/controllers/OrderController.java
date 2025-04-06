package dev.centraluniversity.marketplace.controllers;

import dev.centraluniversity.marketplace.models.Order;
import dev.centraluniversity.marketplace.models.OrderStatus;
import dev.centraluniversity.marketplace.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Find order by ID", description = "Find order by id")
    @ApiResponse(responseCode = "200", description = "Order found")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @Operation(summary = "Update order status", description = "Updates an existing order status")
    @ApiResponse(responseCode = "200", description = "Order updated successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @PatchMapping("/{id}/status")
    public Order updateOrderStatus(
            @PathVariable Long id,
            @RequestParam @Valid OrderStatus status) {
        return orderService.updateStatus(id, status);
    }
}