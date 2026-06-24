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

@Service
public class DeleteBookService {

    private final BookRepository bookRepository;

    public DeleteBookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

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
