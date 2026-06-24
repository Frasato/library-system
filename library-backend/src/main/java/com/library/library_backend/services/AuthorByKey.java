package com.library.library_backend.services;

import com.library.library_backend.dto.AuthorResponseOpenLibraryDto;
import com.library.library_backend.exceptions.AuthorNotFoundException;
import com.library.library_backend.exceptions.ExternalAPIException;
import com.library.library_backend.exceptions.InvalidKeyException;
import com.library.library_backend.mappers.AuthorMapper;
import com.library.library_backend.models.Author;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthorByKey {
    private final RestTemplate restTemplate;
    private final AuthorMapper authorMapper;

    public AuthorByKey(RestTemplate restTemplate, AuthorMapper authorMapper){
        this.restTemplate = restTemplate;
        this.authorMapper = authorMapper;
    }

    public Author fetch(String key){
        if(key.isEmpty()) throw new InvalidKeyException();

        try{
            String uri = "https://openlibrary.org"+ key +".json";
            AuthorResponseOpenLibraryDto response = restTemplate.getForObject(uri, AuthorResponseOpenLibraryDto.class);

            if(response != null){
                Author author = authorMapper.toEntity(response);
                author.setKey(key);
                return author;
            }else{
                throw new AuthorNotFoundException(key);
            }

        }catch(RestClientException e){
            throw new ExternalAPIException("Error calling OpenLibrary API: " + e);
        }
    }
}
