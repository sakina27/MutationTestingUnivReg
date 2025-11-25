package com.example.university.exception;

public class InvalidSemesterException extends RuntimeException {
    public InvalidSemesterException(String message) {
        super(message);
    }
}
