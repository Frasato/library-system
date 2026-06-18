package com.library.library_backend.services;

import com.library.library_backend.dto.AuthorResponseOpenLibraryDto;
import com.library.library_backend.models.Author;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthorByKey {
    private final RestTemplate restTemplate;

    public AuthorByKey(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public Author fetch(String key){
        if(key.isEmpty()) throw new RuntimeException("Key can't be empty!");

        try{
            String uri = "https://openlibrary.org"+ key +".json";
            AuthorResponseOpenLibraryDto response = restTemplate.getForObject(uri, AuthorResponseOpenLibraryDto.class);

            if(response != null){
                return response.toEntity();
            }else{
                throw new RuntimeException("Body not found!");
            }

        }catch(RestClientException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
