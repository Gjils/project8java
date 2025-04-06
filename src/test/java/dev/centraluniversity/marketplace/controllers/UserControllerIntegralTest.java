package dev.centraluniversity.marketplace.controllers;

import dev.centraluniversity.marketplace.dto.FavoriteDto;
import dev.centraluniversity.marketplace.dto.ProductDto;
import dev.centraluniversity.marketplace.dto.UserDto;
import dev.centraluniversity.marketplace.models.Favorite;
import dev.centraluniversity.marketplace.models.Product;
import dev.centraluniversity.marketplace.models.User;
import dev.centraluniversity.marketplace.repositories.FavoriteRepository;
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
public class UserControllerIntegralTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.1")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");
    @Autowired
    private FavoriteRepository favoriteRepository;

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
    public void testAddFavorite() {
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

        FavoriteDto favoriteDto = new FavoriteDto();
        favoriteDto.setProductId(productResponse.getBody().getId());

        testRestTemplate.postForObject("/users/" + userResponse.getBody().getId() + "/favorites", favoriteDto, Object.class);

        Favorite saved = favoriteRepository.findByUserIdAndProductId(userResponse.getBody().getId(), productResponse.getBody().getId()).orElse(null);
        assertNotNull(saved);
    }
}
