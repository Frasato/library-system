package com.library.library_backend.exceptions;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String id) {
        super("Book not found on: " + id);
    }
}
