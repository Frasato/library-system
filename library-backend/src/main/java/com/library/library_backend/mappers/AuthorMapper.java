package com.library.library_backend.mappers;

import com.library.library_backend.dto.AuthorResponseOpenLibraryDto;
import com.library.library_backend.models.Author;

/**
 * Mapper responsável pela conversão entre {@link AuthorResponseOpenLibraryDto} e {@link Author}.
 */
public class AuthorMapper {

    /**
     * Converte um {@link AuthorResponseOpenLibraryDto} para a entidade {@link Author}.
     *
     * <p>Mapeamento dos campos:</p>
     * <ul>
     *   <li>{@code name} → {@code nome}</li>
     *   <li>{@code birth_date} → {@code dataNasc}</li>
     * </ul>
     *
     * <p><b>Atenção:</b> o campo {@code key} <b>não é mapeado</b> aqui pois não está presente
     * no corpo da resposta da API — ele é aplicado manualmente após a conversão
     * via {@link com.library.library_backend.services.AuthorByKey#fetch(String)}.</p>
     *
     * @param response DTO com os dados retornados pela OpenLibrary.
     * @return {@link Author} com os campos mapeados. O campo {@code key} será {@code null}.
     */
    public Author toEntity(AuthorResponseOpenLibraryDto response){
        Author author = new Author();
        author.setNome(response.name());
        author.setDataNasc(response.birth_date());
        return author;
    }

}