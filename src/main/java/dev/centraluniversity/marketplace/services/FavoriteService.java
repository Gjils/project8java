package dev.centraluniversity.marketplace.services;

import dev.centraluniversity.marketplace.models.Favorite;
import dev.centraluniversity.marketplace.repositories.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public List<Favorite> getUserFavorites(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    public Favorite addFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    public void removeFavorite(Long userId, Long productId) {
        favoriteRepository.deleteByUserIdAndProductId(userId, productId);
    }

    public boolean isFavorite(Long userId, Long productId) {
        return favoriteRepository.findByUserIdAndProductId(userId, productId).isPresent();
    }
}
