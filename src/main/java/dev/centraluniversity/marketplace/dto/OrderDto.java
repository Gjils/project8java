package dev.centraluniversity.marketplace.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class OrderDto {
    private LocalDateTime orderDate;

    @Size(max = 50)
    private String status;
}