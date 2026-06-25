package com.library.library_backend.services;

import com.library.library_backend.models.Book;
import com.library.library_backend.repositories.BookRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service responsável por retornar todos os livros cadastrados.
 */
@Service
public class AllBooksService {

    private final BookRepository bookRepository;

    /**
     * @param bookRepository repositório para listagem dos livros.
     */
    public AllBooksService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Retorna todos os livros cadastrados no banco de dados.
     *
     * <p>O resultado é armazenado em cache para reduzir consultas
     * repetidas ao banco. O cache é invalidado quando operações
     * de importação ou atualização modificam os livros.</p>
     *
     * @return lista de {@link Book}. Retorna lista vazia caso não haja registros.
     */
    @Cacheable("books")
    public List<Book> fetchAllBooks(){
        return bookRepository.findAll();
    }
}
