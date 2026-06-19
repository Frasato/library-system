package com.library.library_backend.services;

import com.library.library_backend.dto.BookResponseOpenLibraryDto;
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
        try{
            String uri = "https://openlibrary.org/isbn/"+ isbn +".json";
            BookResponseOpenLibraryDto response = restTemplate.getForObject(uri, BookResponseOpenLibraryDto.class);

            if(response != null){
                return response;
            }else{
                throw new RuntimeException("No body founded");
            }

        }catch (RestClientException e){
            throw new RuntimeException(e.getMessage() + " | " + e.getCause());
        }
    }
}