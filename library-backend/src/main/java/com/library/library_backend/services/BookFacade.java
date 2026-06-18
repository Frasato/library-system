package com.library.library_backend.services;

import com.library.library_backend.dto.BookResponseOpenLibraryDto;
import com.library.library_backend.mappers.BookMapper;
import com.library.library_backend.models.Author;
import com.library.library_backend.models.Book;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookFacade {
    private final BookByIsbnService bookByIsbnService;
    private final AuthorByKey authorByKey;
    private final BookMapper bookMapper;

    public BookFacade(BookByIsbnService bookByIsbnService, AuthorByKey authorByKey, BookMapper bookMapper){
        this.bookByIsbnService = bookByIsbnService;
        this.authorByKey = authorByKey;
        this.bookMapper = bookMapper;
    }

    public Book getInfosByIsbn(String isbn){
        BookResponseOpenLibraryDto responseBook = bookByIsbnService.fetch(isbn);
        Book book = bookMapper.toEntity(responseBook);

        List<Author> authors = responseBook.authors()
                .stream()
                .map(authorKey -> authorByKey.fetch(authorKey.key()))
                .toList();

        book.setAuthor(authors);
        return book;
    }

}
