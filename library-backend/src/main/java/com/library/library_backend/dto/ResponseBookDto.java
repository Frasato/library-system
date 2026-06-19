package com.library.library_backend.dto;

import com.library.library_backend.models.Author;

import java.util.List;

public record ResponseBookDto(
        String title,
        String publishDate,
        List<String> publisher,
        List<Author> authors
){}