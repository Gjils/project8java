package dev.centraluniversity.marketplace.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderItemDto {
    @NotNull
    private Integer orderId;

    @NotNull
    private Integer productId;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotNull
    @Min(0)
    private Double price;
}