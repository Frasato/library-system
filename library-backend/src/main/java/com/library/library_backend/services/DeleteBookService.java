package com.library.library_backend.services;

import com.library.library_backend.dto.ResponseDeleteBookDto;
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

    public ResponseDeleteBookDto removeBook(UUID id){
        Optional<Book> foundedBook = bookRepository.findById(id);
        foundedBook.ifPresent(bookRepository::delete);

        return new ResponseDeleteBookDto(id.toString(), "deleted", Instant.now());
    }
}
