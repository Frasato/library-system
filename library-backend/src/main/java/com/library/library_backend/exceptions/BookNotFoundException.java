package com.library.library_backend.exceptions;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String id) {
        super("Book not found on: " + id);
    }
}
