package dev.centraluniversity.marketplace.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class OrderDto {
    public List<OrderItemDto> items;
}
