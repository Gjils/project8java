package dev.centraluniversity.marketplace.controllers;

import dev.centraluniversity.marketplace.dto.*;
import dev.centraluniversity.marketplace.models.*;
import dev.centraluniversity.marketplace.services.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management")
public class UserController {
    private final UserService userService;
    private final OrderService orderService;
    private final FavoriteService favoriteService;
    private final ProductService productService;
    private final ReviewService reviewService;

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
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Create a new user", description = "Registers a new user in the system")
    @ApiResponse(responseCode = "200", description = "User created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid user data")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAddress(userDto.getAddress());
        user.setPhone(userDto.getPhone());

        User created = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update user", description = "Updates an existing user's information")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody @Valid UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAddress(userDto.getAddress());
        user.setPhone(userDto.getPhone());

        return userService.updateUser(id, user);
    }

    @Operation(summary = "Delete user", description = "Removes a user from the system")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create a new order", description = "Add a new order in the database")
    @ApiResponse(responseCode = "200", description = "Order created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid order data")
    @PostMapping("/{id}/orders")
    public ResponseEntity<Order> createOrder(@PathVariable Long id, @RequestBody @Valid OrderDto orderDto) {
        Order order = new Order();
        User user = userService.getUserById(id);
        order.setUser(user);

        List<OrderItem> items = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderDto.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setPrice(orderItemDto.getPrice());
            orderItem.setOrder(order);
            items.add(orderItem);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(order, items));
    }

    @GetMapping("{id}/orders")
    @Operation(summary = "Find orders by user ID", description = "Get all orders for a specific user")
    @ApiResponse(responseCode = "200", description = "List of user orders")
    public List<Order> getOrdersByUser(@PathVariable Long id) {
        return orderService.getUserOrders(id);
    }

    @PostMapping("{id}/favorites")
    @Operation(summary = "Add favorite", description = "Add favorite product to user")
    @ApiResponse(responseCode = "200", description = "Created favorite product")
    public ResponseEntity<Favorite> addFavorite(@PathVariable Long id, @RequestBody @Valid FavoriteDto favoriteDto) {
        Long productId = favoriteDto.getProductId();
        Product product = productService.getProduct(productId);
        User user = userService.getUserById(id);
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(favoriteService.addFavorite(favorite));
    }

    @GetMapping("{id}/favorites")
    @Operation(summary = "User favorites", description = "All favorite product of user")
    @ApiResponse(responseCode = "200", description = "List of user favorite products")
    public List<Favorite> getFavorites(@PathVariable Long id) {
        return favoriteService.getUserFavorites(id);
    }

    @PostMapping("{id}/review")
    @Operation(summary = "Add review", description = "Add favorite product to user")
    @ApiResponse(responseCode = "200", description = "Created review")
    public ResponseEntity<ReviewDto> addReview(@PathVariable Long id, @RequestBody @Valid ReviewDto reviewDto) {
        Review review = new Review();
        User user = userService.getUserById(id);
        Product product = productService.getProduct(reviewDto.getProductId());
        review.setUser(user);
        review.setProduct(product);
        review.setComment(reviewDto.getComment());
        review.setRating(reviewDto.getRating());
        review = reviewService.addReview(review);
        reviewDto.setId(review.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewDto);
    }

    @Operation(summary = "Remove favorite", description = "Removes a favorite from user")
    @ApiResponse(responseCode = "200", description = "Favorite deleted successfully")
    @ApiResponse(responseCode = "404", description = "Not favorite")
    @DeleteMapping("/{id}/favorites")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id, @RequestBody @Valid FavoriteDto favoriteDto) {
        favoriteService.removeFavorite(id, favoriteDto.getProductId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove review", description = "Removes a review from user")
    @ApiResponse(responseCode = "200", description = "Review deleted successfully")
    @ApiResponse(responseCode = "404", description = "No review")
    @DeleteMapping("/{id}/review")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, @RequestBody @Valid ReviewDeleteDto reviewDeleteDto) {
        Review review = reviewService.getReview(reviewDeleteDto.getReviewId());
        reviewService.deleteReview(review);
        return ResponseEntity.noContent().build();
    }
}
