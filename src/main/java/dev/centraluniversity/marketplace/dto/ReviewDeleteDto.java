package dev.centraluniversity.marketplace.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema
public class ReviewDeleteDto {
    @NotNull
    private Long reviewId;
}
