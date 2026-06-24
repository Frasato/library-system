package com.library.library_backend.services;

import com.library.library_backend.dto.*;
import com.library.library_backend.mappers.BookMapper;
import com.library.library_backend.models.Author;
import com.library.library_backend.models.Book;
import com.library.library_backend.services.importers.ImportBookService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
    private final UpdateBookService updateBookService;
    private final ImportBookService importBookService;

    public BookFacade(BookByIsbnService bookByIsbnService, AuthorByKey authorByKey, BookMapper bookMapper, CreateBookService createBookService, AllBooksService allBooksService, DeleteBookService deleteBookService, UpdateBookService updateBookService, ImportBookService importBookService){
        this.bookByIsbnService = bookByIsbnService;
        this.authorByKey = authorByKey;
        this.bookMapper = bookMapper;
        this.createBookService = createBookService;
        this.allBooksService = allBooksService;
        this.deleteBookService = deleteBookService;
        this.updateBookService = updateBookService;
        this.importBookService = importBookService;
    }

    public ResponseBookDto getInfosByIsbn(String isbn){
        BookResponseOpenLibraryDto responseBook = bookByIsbnService.fetch(isbn);
        Book book = bookMapper.toEntity(responseBook);

        if(responseBook.authors() != null){
            List<Author> authors = responseBook.authors()
                    .stream()
                    .map(authorKey -> authorByKey.fetch(authorKey.key()))
                    .toList();

            book.setAuthor(authors);
        }

        return bookMapper.toDto(book);
    }

    public ResponseBookDto createNewBook(String isbn){
        BookResponseOpenLibraryDto responseBook = bookByIsbnService.fetch(isbn);
        Book book = bookMapper.toEntity(responseBook);

        if(responseBook.authors() != null){
            List<Author> authors = responseBook.authors()
                    .stream()
                    .map(authorKey -> authorByKey.fetch(authorKey.key()))
                    .toList();

            book.setAuthor(authors);
        }

        Book savedBook = createBookService.createBook(book);
        return bookMapper.toDto(savedBook);
    }

    public ResponseImportBookDto importBook(MultipartFile file){
        return importBookService.importFile(file);
    }

    public ResponseUpdateBookDto updateBook(RequestUpdateBookDto requestUpdateBookDto, String id){ return updateBookService.updateBook(requestUpdateBookDto, UUID.fromString(id));}

    public List<Book> allBooks(){
        return allBooksService.fetchAllBooks();
    }

    public ResponseDeleteBookDto deleteBook(String id){ return deleteBookService.removeBook(id); }
}
