package com.library.library_backend.mappers;

import com.library.library_backend.dto.BookResponseOpenLibraryDto;
import com.library.library_backend.dto.ResponseBookDto;
import com.library.library_backend.models.Book;

/**
 * Mapper responsável pelas conversões entre {@link Book}, {@link BookResponseOpenLibraryDto}
 * e {@link ResponseBookDto}.
 */
public class BookMapper {

    /**
     * Converte uma entidade {@link Book} para {@link ResponseBookDto}.
     *
     * @param book entidade a ser convertida.
     * @return {@link ResponseBookDto} com os dados do livro.
     */
    public ResponseBookDto toDto(Book book){
        return new ResponseBookDto(
                book.getTitulo(),
                book.getDataPublicacao(),
                book.getEditora(),
                book.getAuthor()
        );
    }

    /**
     * Converte um {@link BookResponseOpenLibraryDto} para a entidade {@link Book}.
     *
     * <p>Mapeamento dos campos:</p>
     * <ul>
     *   <li>{@code title} → {@code titulo}</li>
     *   <li>{@code publishers} → {@code editora}</li>
     *   <li>{@code isbn_13} → {@code isbn}</li>
     *   <li>{@code publish_date} → {@code dataPublicacao}</li>
     * </ul>
     *
     * <p><b>Atenção:</b> os autores <b>não são mapeados</b> aqui por dependerem
     * de uma chamada separada à API via {@link com.library.library_backend.services.AuthorByKey}.</p>
     *
     * @param responseOpenLibraryDto DTO com os dados retornados pela OpenLibrary.
     * @return {@link Book} com os campos mapeados. O campo {@code author} será {@code null}.
     */
    public Book toEntity(BookResponseOpenLibraryDto responseOpenLibraryDto){
        Book book = new Book();
        book.setTitulo(responseOpenLibraryDto.title());
        book.setEditora(responseOpenLibraryDto.publishers());
        book.setIsbn(responseOpenLibraryDto.isbn_13());
        book.setDataPublicacao(responseOpenLibraryDto.publish_date());
        return book;
    }
}
