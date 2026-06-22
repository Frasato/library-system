package com.library.library_backend.dto;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public record ResponseImportBookDto(
        Object status,
        AtomicInteger rows_updated,
        AtomicInteger rows_added,
        Instant time
){}
