package dev.centraluniversity.marketplace.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema
public class ReviewDto {
    private Long id;

    @NotNull
    private Long productId;

    @Size(max = 500)
    private String comment;

    @Min(1)
    @Min(5)
    private Integer rating;

}
