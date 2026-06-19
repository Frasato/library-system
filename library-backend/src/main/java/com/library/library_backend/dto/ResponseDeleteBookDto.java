package com.library.library_backend.dto;

import java.time.Instant;

public record ResponseDeleteBookDto(
        String id,
        String status,
        Instant deletedAt
){}
