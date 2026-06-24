package com.library.library_backend.exceptions;

public class InvalidFileException extends RuntimeException {
    public InvalidFileException() {
        super("No file found!");
    }
}
