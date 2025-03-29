package dev.centraluniversity.marketplace.services;

import dev.centraluniversity.marketplace.dto.ProductDto;
import dev.centraluniversity.marketplace.models.Product;
import dev.centraluniversity.marketplace.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContaining(name);
    }

    public Product createProduct(ProductDto productDto) {
        Product product = new Product(
                null,
                productDto.getName(),
                productDto.getDescription(),
                productDto.getPrice(),
                productDto.getCategory());
        return productRepository.save(product);
    }

    public Optional<Product> updateProduct(Long id, ProductDto productDto) {
        return productRepository.findById(id).map(product -> {
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setCategory(productDto.getCategory());
            return productRepository.update(product);
        });
    }

    public boolean deleteProduct(Long id) {
        return productRepository.delete(id);
    }
}