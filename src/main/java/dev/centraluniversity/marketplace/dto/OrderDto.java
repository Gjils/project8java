package dev.centraluniversity.marketplace.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Schema
public class OrderDto {
    public List<OrderItemDto> items;
}
