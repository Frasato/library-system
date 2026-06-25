package com.library.library_backend.services;

import com.library.library_backend.models.Author;
import com.library.library_backend.models.Book;
import com.library.library_backend.repositories.AuthorRepository;
import com.library.library_backend.repositories.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service responsável pela persistência de novos livros.
 *
 * <p>Aplica lógica de <b>upsert nos autores</b>: autores já existentes
 * (identificados pela Key) são reutilizados, e autores novos são inseridos.</p>
 *
 * @see AuthorRepository
 * @see BookRepository
 */
@Service
public class CreateBookService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    /**
     * @param authorRepository repositório para busca e persistência dos autores.
     * @param bookRepository repositório para persistência dos livros.
     */
    public CreateBookService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * Persiste um novo livro e seus autores no banco de dados.
     *
     * <p>Para cada autor do livro:</p>
     * <ul>
     *   <li>Se a Key já existir no banco — <b>reutiliza</b> o autor existente</li>
     *   <li>Se a Key não existir — <b>insere</b> um novo autor</li>
     * </ul>
     *
     * <p><b>Cache:</b> após a conclusão da criação, todas as entradas
     * do cache {@code books} são invalidadas para garantir que futuras
     * consultas retornem dados atualizados.</p>
     *
     * <p>Toda a operação é executada em uma única transação via {@code @Transactional},
     * garantindo que livro e autores sejam salvos juntos ou revertidos em caso de falha.</p>
     *
     * @param book entidade {@link Book} a ser persistida, com seus autores já populados.
     * @return {@link Book} salvo com ID gerado e autores persistidos.
     */
    @Transactional
    @CacheEvict(value = "books", allEntries = true)
    public Book createBook(Book book){
        List<Author> authors = book.getAuthor();
        if(authors != null){
            List<Author> savedAuthors = authors
                    .stream()
                    .map(author -> authorRepository
                            .findAuthorByKey(author.getKey())
                            .orElseGet(() -> authorRepository.save(author)))
                    .toList();
    
            book.setAuthor(savedAuthors);
        }

        return bookRepository.save(book);
    }

}
