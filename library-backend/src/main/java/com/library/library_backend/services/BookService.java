package com.library.library_backend.services;

import com.library.library_backend.models.Book;
import com.library.library_backend.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    private final RestTemplate restTemplate;

    public BookService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public Book getBookByIsbn(String isbn){
        if(isbn.isEmpty()) throw new RuntimeException("ISBN não pode ser vazio!");

        String uri = "https://openlibrary.org/isbn/"+ isbn +".json";

    }

}