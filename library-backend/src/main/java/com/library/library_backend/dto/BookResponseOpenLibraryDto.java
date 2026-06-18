package com.library.library_backend.dto;

import java.util.List;

public record BookResponseOpenLibraryDto(
        String title,
        List<AuthorKeyDto> authors,
        List<WorksDto> works,
        List<String> publishers,
        List<String> isbn_13,
        String publish_date
){}
