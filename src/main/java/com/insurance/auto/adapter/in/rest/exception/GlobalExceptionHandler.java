package com.insurance.auto.adapter.in.rest.exception;

import com.insurance.auto.domain.exception.RejectException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RejectException.class)
    public ResponseEntity<Map<String, String>> handleReject(RejectException e) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("message", e.getMessage()));
    }
}
