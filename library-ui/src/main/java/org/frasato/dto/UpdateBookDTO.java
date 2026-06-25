package org.frasato.dto;

import java.util.List;

public record UpdateBookDTO(
        String titulo,
        String dataPublicacao,
        List<String> isbn,
        List<String> editora,
        List<String> autores,
        List<String> livrosSemelhantes
){}
