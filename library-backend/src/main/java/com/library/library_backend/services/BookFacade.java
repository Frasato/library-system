package com.library.library_backend.services;

import com.library.library_backend.dto.BookResponseOpenLibraryDto;
import com.library.library_backend.dto.ResponseBookDto;
import com.library.library_backend.dto.ResponseDeleteBookDto;
import com.library.library_backend.mappers.BookMapper;
import com.library.library_backend.models.Author;
import com.library.library_backend.models.Book;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class BookFacade {
    private final BookByIsbnService bookByIsbnService;
    private final AuthorByKey authorByKey;
    private final BookMapper bookMapper;
    private final CreateBookService createBookService;
    private final AllBooksService allBooksService;
    private final DeleteBookService deleteBookService;

    public BookFacade(BookByIsbnService bookByIsbnService, AuthorByKey authorByKey, BookMapper bookMapper, CreateBookService createBookService, AllBooksService allBooksService, DeleteBookService deleteBookService){
        this.bookByIsbnService = bookByIsbnService;
        this.authorByKey = authorByKey;
        this.bookMapper = bookMapper;
        this.createBookService = createBookService;
        this.allBooksService = allBooksService;
        this.deleteBookService = deleteBookService;
    }

    public ResponseBookDto getInfosByIsbn(String isbn){
        if(isbn.isEmpty()) throw new RuntimeException("ISBN can't be empty!");

        BookResponseOpenLibraryDto responseBook = bookByIsbnService.fetch(isbn);
        Book book = bookMapper.toEntity(responseBook);

        List<Author> authors = responseBook.authors()
                .stream()
                .map(authorKey -> authorByKey.fetch(authorKey.key()))
                .toList();

        book.setAuthor(authors);
        return bookMapper.toDto(book);
    }

    public ResponseBookDto createNewBook(String isbn){
        if(isbn.isEmpty()) throw new RuntimeException("ISBN can't be empty!");

        BookResponseOpenLibraryDto responseBook = bookByIsbnService.fetch(isbn);
        Book book = bookMapper.toEntity(responseBook);

        List<Author> authors = responseBook.authors()
                .stream()
                .map(authorKey -> authorByKey.fetch(authorKey.key()))
                .toList();

        book.setAuthor(authors);

        Book savedBook = createBookService.createBook(book);
        return bookMapper.toDto(savedBook);
    }

    public List<Book> allBooks(){
        return allBooksService.fetchAllBooks();
    }

    public ResponseDeleteBookDto deleteBook(String id){
        if(id.isEmpty()) throw new RuntimeException("ID can't be null");
        UUID convertedId = UUID.fromString(id);
        return deleteBookService.removeBook(convertedId);
    }

}
