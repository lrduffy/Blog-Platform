package com.strawhats.blogplatform.exception;

public class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
    }
}
