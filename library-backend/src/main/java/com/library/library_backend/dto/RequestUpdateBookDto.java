package com.library.library_backend.dto;

import java.util.List;

public record RequestUpdateBookDto(
        String titulo,
        String dataPublicacao,
        List<String> isbn,
        List<String> editora,
        List<String> livrosSemelhantes
){}
