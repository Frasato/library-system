package com.library.library_backend.services;

import com.library.library_backend.dto.BookResponseOpenLibraryDto;
import com.library.library_backend.models.Book;
import com.library.library_backend.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
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
        if(isbn.isEmpty()) throw new RuntimeException("ISBN can't be empty!");

        try{
            String uri = "https://openlibrary.org/isbn/"+ isbn +".json";
            BookResponseOpenLibraryDto responseOpenLibraryDto = restTemplate.getForObject(uri, BookResponseOpenLibraryDto.class);

            if(responseOpenLibraryDto != null){
                return responseOpenLibraryDto.toEntity();
            }else{
                throw new RuntimeException("No body founded");
            }

        }catch (RestClientException e){
            throw new RuntimeException(e.getMessage());
        }
    }

}