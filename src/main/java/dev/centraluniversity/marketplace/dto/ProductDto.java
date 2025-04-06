package dev.centraluniversity.marketplace.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
@Schema
public class ProductDto {
    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    @NotNull
    @Min(0)
    private BigDecimal price;

    @Size(max = 100)
    private String category;


}
