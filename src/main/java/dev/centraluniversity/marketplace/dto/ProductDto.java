package dev.centraluniversity.marketplace.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductDto {
    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    @NotNull
    @Min(0)
    private Double price;

    @Size(max = 100)
    private String category;
}
