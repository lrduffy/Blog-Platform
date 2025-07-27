package com.strawhats.blogplatform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> userAlreadyExistsExceptionHandler(UserAlreadyExistsException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("Error", "User with provided crdentials already exists!");
        response.put("Message", e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> badCredentialsExceptionHandler(BadCredentialsException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("Error", "Invalid username or password");
        response.put("Message", e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> apiExceptionHandler(ApiException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("Error", e.getMessage());
        response.put("Success", "false");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> resourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("Error", "Resource not found!");
        response.put("Message", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> exceptionHandler(Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("Error", "Internal Server Error");
        response.put("Message", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
