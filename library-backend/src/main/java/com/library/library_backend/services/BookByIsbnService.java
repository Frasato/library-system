package com.library.library_backend.services;

import com.library.library_backend.dto.BookResponseOpenLibraryDto;
import com.library.library_backend.exceptions.BookNotFoundException;
import com.library.library_backend.exceptions.ExternalAPIException;
import com.library.library_backend.exceptions.InvalidISBNException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class BookByIsbnService {
    private final RestTemplate restTemplate;

    public BookByIsbnService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public BookResponseOpenLibraryDto fetch(String isbn){
        if(isbn.isEmpty()) throw new InvalidISBNException();

        try{
            String uri = "https://openlibrary.org/isbn/"+ isbn +".json";
            BookResponseOpenLibraryDto response = restTemplate.getForObject(uri, BookResponseOpenLibraryDto.class);

            if(response != null){
                return response;
            }else{
                throw new BookNotFoundException(isbn);
            }

        }catch (RestClientException e){
            throw new ExternalAPIException("Error calling OpenLibrary API: " + e);
        }
    }
}