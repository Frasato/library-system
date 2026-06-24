package com.library.library_backend.exceptions;

public class UnsupportedFileException extends RuntimeException {
    public UnsupportedFileException() {
        super("File not supported!");
    }
}
