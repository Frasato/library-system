package com.library.library_backend.dto;

public record AuthorResponseOpenLibraryDto(
        String birth_date,
        String name,
        BioDto bio
){}