package dev.centraluniversity.marketplace.repositories;

import dev.centraluniversity.marketplace.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductId(Long productId);

    List<Review> findByProductIdOrderByCreatedAtDesc(Long productId);

    List<Review> findByProductIdOrderByRatingDesc(Long productId);

    List<Review> findByUserId(Long userId);
}
