package dev.centraluniversity.marketplace.controllers;

import dev.centraluniversity.marketplace.dto.ProductDto;
import dev.centraluniversity.marketplace.models.Product;
import dev.centraluniversity.marketplace.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String name) {
        return productService.searchProductsByName(name);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductDto productDto) {
        Product createdProduct = productService.createProduct(productDto);
        return ResponseEntity.ok(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody @Valid ProductDto productDto) {
        return productService.updateProduct(id, productDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}