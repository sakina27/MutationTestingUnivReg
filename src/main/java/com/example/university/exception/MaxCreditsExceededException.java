package com.example.university.exception;

public class MaxCreditsExceededException extends RuntimeException {
    public MaxCreditsExceededException(String message) {
        super(message);
    }
}
