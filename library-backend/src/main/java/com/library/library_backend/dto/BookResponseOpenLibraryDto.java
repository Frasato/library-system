package com.library.library_backend.dto;

import java.util.List;

public record BookResponseOpenLibraryDto(
        String title,
        List<String> authors,
        List<String> publishers,
        List<String> isbn_13,
        Created created
){}

record Created(
        String value
){}
