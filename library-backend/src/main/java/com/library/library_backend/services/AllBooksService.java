package com.library.library_backend.services;

import com.library.library_backend.models.Book;
import com.library.library_backend.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllBooksService {

    private final BookRepository bookRepository;

    public AllBooksService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> fetchAllBooks(){
        return bookRepository.findAll();
    }
}
