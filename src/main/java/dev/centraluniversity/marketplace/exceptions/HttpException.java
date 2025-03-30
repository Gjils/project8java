package dev.centraluniversity.marketplace.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public class HttpException extends RuntimeException {
    private final HttpStatus status;

    public HttpException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
