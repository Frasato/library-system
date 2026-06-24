package com.library.library_backend.exceptions;

public class InvalidFileNameException extends RuntimeException {
    public InvalidFileNameException() {
        super("File name is not valid!");
    }
}
