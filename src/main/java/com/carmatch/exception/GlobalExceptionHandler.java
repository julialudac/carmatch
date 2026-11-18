package com.carmatch.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    // public ResponseEntity<Void> getBadRequest() {
    // public ResponseEntity<List<FieldError>> getBadRequest(MethodArgumentNotValidException ex) {
    public ResponseEntity<List<String>> getBadRequest(MethodArgumentNotValidException ex) {
        // return ResponseEntity.badRequest().build();
        // return ResponseEntity.badRequest().body(ex.getBindingResult().getFieldErrors());
        return ResponseEntity.badRequest().body(ex.getBindingResult().getFieldErrors()
                .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
    }
}
