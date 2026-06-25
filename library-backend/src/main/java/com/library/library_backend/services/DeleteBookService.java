package com.library.library_backend.services;

import com.library.library_backend.dto.ResponseDeleteBookDto;
import com.library.library_backend.exceptions.BookNotFoundException;
import com.library.library_backend.exceptions.InvalidIDException;
import com.library.library_backend.models.Book;
import com.library.library_backend.repositories.BookRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Service responsável pela exclusão de livros.
 *
 * @see BookRepository
 */
@Service
public class DeleteBookService {

    private final BookRepository bookRepository;

    /**
     * @param bookRepository repositório para busca e exclusão dos livros.
     */
    public DeleteBookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Remove um livro pelo seu ID.
     *
     * <p>O ID é validado e convertido de {@link String} para {@link UUID}
     * antes da busca no repositório.</p>
     *
     * @param id identificador único do livro a ser removido em formato {@link String}.
     * @return {@link ResponseDeleteBookDto} com o ID, status {@code "deleted"} e data da exclusão.
     * @throws InvalidIDException    caso o ID informado seja vazio.
     * @throws BookNotFoundException caso nenhum livro seja encontrado com o ID informado.
     * @throws IllegalArgumentException caso o ID não seja um UUID válido.
     */
    public ResponseDeleteBookDto removeBook(String id){
        if(id.isEmpty()) throw new InvalidIDException();
        UUID convertedId = UUID.fromString(id);

        Optional<Book> foundedBook = bookRepository.findById(convertedId);

        if(foundedBook.isEmpty()) throw new BookNotFoundException(id);

        Book book = foundedBook.get();
        bookRepository.delete(book);

        return new ResponseDeleteBookDto(id, "deleted", Instant.now());
    }
}
