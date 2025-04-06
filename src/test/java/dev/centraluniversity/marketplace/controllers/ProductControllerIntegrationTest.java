package dev.centraluniversity.marketplace.controllers;

import dev.centraluniversity.marketplace.dto.ProductDto;
import dev.centraluniversity.marketplace.dto.ReviewDto;
import dev.centraluniversity.marketplace.dto.UserDto;
import dev.centraluniversity.marketplace.models.Product;
import dev.centraluniversity.marketplace.models.User;
import dev.centraluniversity.marketplace.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
public class ProductControllerIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.1")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testCreateProduct() {
        ProductDto productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setPrice(new BigDecimal("199.99"));
        productDto.setCategory("Electronics");
        productDto.setDescription("Test Description");

        ResponseEntity<Product> response = testRestTemplate.postForEntity("/products", productDto, Product.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Product saved = productRepository.findById(response.getBody().getId()).orElse(null);
        assertNotNull(saved);
        assertEquals("Test Product", saved.getName());
        assertEquals(new BigDecimal("199.99"), saved.getPrice());
    }

    @Test
    public void testUpdateProduct() {
        ProductDto productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setPrice(new BigDecimal("199.99"));
        productDto.setCategory("Electronics");
        productDto.setDescription("Test Description");

        ResponseEntity<Product> response = testRestTemplate.postForEntity("/products", productDto, Product.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        productDto.setPrice(new BigDecimal("299.99"));
        testRestTemplate.put("/products/" + response.getBody().getId(), productDto, Product.class);

        Product saved = productRepository.findById(response.getBody().getId()).orElse(null);
        assertNotNull(saved);
        assertEquals("Test Product", saved.getName());
        assertEquals(new BigDecimal("299.99"), saved.getPrice());
    }

    @Test
    public void testDeleteProduct() {
        ProductDto productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setPrice(new BigDecimal("199.99"));
        productDto.setCategory("Electronics");
        productDto.setDescription("Test Description");

        ResponseEntity<Product> response = testRestTemplate.postForEntity("/products", productDto, Product.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        testRestTemplate.delete("/products/" + response.getBody().getId());

        Product saved = productRepository.findById(response.getBody().getId()).orElse(null);
        assertNull(saved);
    }

    @Test
    public void testFilterProduct() {
        ProductDto productDto1 = new ProductDto();
        productDto1.setName("Test Product");
        productDto1.setPrice(new BigDecimal("199.99"));
        productDto1.setCategory("Electronics");
        productDto1.setDescription("Test Description");

        ProductDto productDto2 = new ProductDto();
        productDto2.setName("Test Product");
        productDto2.setPrice(new BigDecimal("599.99"));
        productDto2.setCategory("Food");
        productDto2.setDescription("Test Description");

        ResponseEntity<Product> response1 = testRestTemplate.postForEntity("/products", productDto1, Product.class);
        ResponseEntity<Product> response2 = testRestTemplate.postForEntity("/products", productDto2, Product.class);

        assertEquals(HttpStatus.CREATED, response1.getStatusCode());
        assertEquals(HttpStatus.CREATED, response2.getStatusCode());

        List<Product> productsByCat = (List<Product>)testRestTemplate.getForObject("/products/findByCategory?category=Food", Object.class);
        assertEquals(1, productsByCat.size());

        List<Product> productsByPrice = (List<Product>)testRestTemplate.getForObject("/products/findByPrice?maxPrice=600", Object.class);
        assertEquals(1, productsByCat.size());
    }

    @Test
    public void testAverageRating() {
        ProductDto productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setPrice(new BigDecimal("199.99"));
        productDto.setCategory("Electronics");
        productDto.setDescription("Test Description");

        UserDto userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test@test.com");
        userDto.setPhone("234");
        userDto.setAddress("Test Address");

        ResponseEntity<Product> productResponse = testRestTemplate.postForEntity("/products", productDto, Product.class);
        ResponseEntity<User> userResponse = testRestTemplate.postForEntity("/users", userDto, User.class);
        assertEquals(HttpStatus.CREATED, productResponse.getStatusCode());
        assertEquals(HttpStatus.CREATED, userResponse.getStatusCode());

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setProductId(productResponse.getBody().getId());
        reviewDto.setRating(3);
        reviewDto.setComment("Test Comment");

        testRestTemplate.postForObject("/users/" + userResponse.getBody().getId() + "/review", reviewDto, Object.class);

        Product saved = productRepository.findById(productResponse.getBody().getId()).orElse(null);
        assertNotNull(saved);
        assertEquals(new BigDecimal("3.00"), saved.getAverageRating());
    }
}