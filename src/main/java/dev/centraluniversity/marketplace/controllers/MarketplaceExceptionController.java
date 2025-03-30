package dev.centraluniversity.marketplace.controllers;

import dev.centraluniversity.marketplace.dto.ErrorDto;
import dev.centraluniversity.marketplace.exceptions.ConflictException;
import dev.centraluniversity.marketplace.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.bind.annotation.ExceptionHandler;

// Этот код все сломал, я не знаю почему
//@ControllerAdvice
//public class MarketplaceExceptionController {
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<ErrorDto> handleNotFoundException(NotFoundException e) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(new ErrorDto(e.getMessage()));
//    }
//
//    @ExceptionHandler(ConflictException.class)
//    public ResponseEntity<ErrorDto> handleNotFoundException(ConflictException e) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(new ErrorDto(e.getMessage()));
//    }
//}
