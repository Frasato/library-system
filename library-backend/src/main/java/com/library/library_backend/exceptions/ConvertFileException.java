package com.library.library_backend.exceptions;

public class ConvertFileException extends RuntimeException {
    public ConvertFileException(String message) {
        super("Error while converting file: " + message);
    }
}
