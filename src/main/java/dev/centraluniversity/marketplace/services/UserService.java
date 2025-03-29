package dev.centraluniversity.marketplace.services;

import dev.centraluniversity.marketplace.dto.UserDto;
import dev.centraluniversity.marketplace.models.Order;
import dev.centraluniversity.marketplace.models.User;
import dev.centraluniversity.marketplace.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(UserDto userDto) {
        User user = new User(
                null,
                userDto.getName(),
                userDto.getEmail(),
                userDto.getAddress(),
                userDto.getPhone(),
                new ArrayList<Order>());
        return userRepository.save(user);
    }

    public Optional<User> updateUser(Long id, UserDto userDto) {
        return userRepository.findById(id).map(user -> {
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setAddress(userDto.getAddress());
            user.setPhone(userDto.getPhone());
            return userRepository.update(user);
        });
    }

    public boolean deleteUser(Long id) {
        return userRepository.delete(id);
    }
}
