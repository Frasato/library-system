package com.library.library_backend.dto;

import java.time.Instant;

public record ResponseImportBookDto(
        Object status,
        int rows_updated,
        int rows_added,
        Instant time
){}
