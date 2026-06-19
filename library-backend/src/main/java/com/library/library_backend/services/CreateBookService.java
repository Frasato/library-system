package com.library.library_backend.services;

import com.library.library_backend.models.Author;
import com.library.library_backend.models.Book;
import com.library.library_backend.repositories.AuthorRepository;
import com.library.library_backend.repositories.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CreateBookService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public CreateBookService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Book createBook(Book book){
        if(book == null) throw new RuntimeException("Book can't be null!");
        if(book.getAuthor().isEmpty()) throw new RuntimeException("Authors can't be empty!");

        List<Author> authors = book.getAuthor();
        List<Author> savedAuthors = authors
                .stream()
                .map(author -> authorRepository
                        .findAuthorByKey(author.getKey())
                        .orElseGet(() -> authorRepository.save(author)))
                .toList();

        book.setAuthor(savedAuthors);
        return bookRepository.save(book);
    }

}
