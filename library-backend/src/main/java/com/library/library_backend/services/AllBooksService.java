package com.library.library_backend.services;

import com.library.library_backend.models.Book;
import com.library.library_backend.repositories.BookRepository;
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
     * @return lista de {@link Book}. Retorna lista vazia caso não haja registros.
     */
    public List<Book> fetchAllBooks(){
        return bookRepository.findAll();
    }
}
