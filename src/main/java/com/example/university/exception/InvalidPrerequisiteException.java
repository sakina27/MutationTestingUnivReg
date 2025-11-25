package com.example.university.exception;

public class InvalidPrerequisiteException extends RuntimeException {
    public InvalidPrerequisiteException(String message) {
        super(message);
    }
}
