package dev.centraluniversity.marketplace.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorDto {
    private final String message;
}
