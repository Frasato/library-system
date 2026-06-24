package com.library.library_backend.dto;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.UUID;

public record ResponseUpdateBookDto(
        HttpStatus status,
        UUID bookId,
        Instant time
){}
