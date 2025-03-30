package dev.centraluniversity.marketplace.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class ErrorDto {
    private final String message;
}
