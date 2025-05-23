package com.app.exception;



import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, Object>> handleUserException(UserException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type", "User Exception");
        errorResponse.put("error", ex.getMessage());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("statusCode", ex.getHttpStatus().toString());

        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(TodoException.class)
    public ResponseEntity<Map<String, Object>> handleTodoException(TodoException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type", "Todo Exception");
        errorResponse.put("error", ex.getMessage());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("statusCode", ex.getHttpStatus().toString());

        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    // Optional: catch-all for unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failure");
        errorResponse.put("type", "Generic Exception");
        errorResponse.put("error", ex.getMessage());
        errorResponse.put("localTime", LocalDateTime.now());
        errorResponse.put("statusCode", "500 INTERNAL_SERVER_ERROR");

        return new ResponseEntity<>(errorResponse, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

