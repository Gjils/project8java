package dev.centraluniversity.marketplace.services;

import dev.centraluniversity.marketplace.models.Product;
import dev.centraluniversity.marketplace.models.Review;
import dev.centraluniversity.marketplace.repositories.ProductRepository;
import dev.centraluniversity.marketplace.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public Review addReview(Review review) {
        Review savedReview = reviewRepository.save(review);
        updateProductRating(savedReview.getProduct().getId());
        return savedReview;
    }

    public List<Review> getProductReviews(Long productId, String sortBy) {
        return switch (sortBy) {
            case "date" -> reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
            case "rating" -> reviewRepository.findByProductIdOrderByRatingDesc(productId);
            default -> reviewRepository.findByProductId(productId);
        };
    }

    private void updateProductRating(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        BigDecimal average = BigDecimal.valueOf(
                reviews.stream()
                        .mapToInt(Review::getRating)
                        .average()
                        .orElse(0.0)
        ).setScale(2, RoundingMode.HALF_UP);

        Product product = productRepository.findById(productId).orElseThrow();
        product.setAverageRating(average);
        productRepository.save(product);
    }

    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow();
    };

    public void deleteReview(Review review) {
        reviewRepository.delete(review);
    }
}