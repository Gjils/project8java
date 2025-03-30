package dev.centraluniversity.marketplace.controllers;

import dev.centraluniversity.marketplace.dto.OrderDto;
import dev.centraluniversity.marketplace.dto.UserDto;
import dev.centraluniversity.marketplace.models.Order;
import dev.centraluniversity.marketplace.models.User;
import dev.centraluniversity.marketplace.services.OrderItemService;
import dev.centraluniversity.marketplace.services.OrderService;
import dev.centraluniversity.marketplace.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management")
public class UserController {
    private final UserService userService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @Operation(summary = "Find all users", description = "Retrieves all users in the system")
    @ApiResponse(responseCode = "200", description = "List of all users")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Find user by ID", description = "Get user by ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new user", description = "Registers a new user in the system")
    @ApiResponse(responseCode = "200", description = "User created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid user data")
    @PostMapping
    public User createUser(@RequestBody @Valid UserDto userDto) {
        return userService.createUser(userDto);
    }

    @Operation(summary = "Update user", description = "Updates an existing user's information")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody @Valid UserDto userDto) {
        return userService.updateUser(id, userDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete user", description = "Removes a user from the system")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Create a new order", description = "Add a new order in the database")
    @ApiResponse(responseCode = "200", description = "Order created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid order data")
    @PostMapping("/{id}/orders")
    public Order createOrder(@PathVariable Long id, @RequestBody @Valid OrderDto orderDto) {
        return orderItemService.createOrder(id, orderDto);
    }

    @GetMapping("{id}/orders")
    @Operation(summary = "Find orders by user ID", description = "Get all orders for a specific user")
    @ApiResponse(responseCode = "200", description = "List of user orders")
    public List<Order> getOrdersByUser(@PathVariable Long userId) {
        return orderService.getOrdersByUserId(userId);
    }
}
