package dev.centraluniversity.marketplace.services;

import dev.centraluniversity.marketplace.models.Product;
import dev.centraluniversity.marketplace.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    public Product createProduct(Product product) {
        product.setAverageRating(BigDecimal.ZERO); // по умолчанию
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updated) {
        Product product = getProduct(id);
        product.setName(updated.getName());
        product.setDescription(updated.getDescription());
        product.setPrice(updated.getPrice());
        product.setCategory(updated.getCategory());
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> filterByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> filterByPrice(BigDecimal min, BigDecimal max) {
        return productRepository.findByPriceBetween(min, max);
    }

    public List<Product> filterByCategoryAndPrice(String category, BigDecimal min, BigDecimal max) {
        return productRepository.findByCategoryAndPriceBetween(category, min, max);
    }
}