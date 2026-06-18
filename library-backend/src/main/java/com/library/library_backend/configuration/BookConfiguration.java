package com.library.library_backend.configuration;

import com.library.library_backend.mappers.AuthorMapper;
import com.library.library_backend.mappers.BookMapper;
import com.library.library_backend.services.AuthorByKey;
import com.library.library_backend.services.BookByIsbnService;
import com.library.library_backend.services.BookFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BookConfiguration {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public BookByIsbnService bookByIsbnService(RestTemplate restTemplate){return new BookByIsbnService(restTemplate);}

    @Bean
    public AuthorByKey authorService(RestTemplate restTemplate, AuthorMapper authorMapper){
        return new AuthorByKey(restTemplate, authorMapper);
    }

    @Bean
    public BookFacade bookFacade(BookByIsbnService bookByIsbnService, AuthorByKey authorByKey, BookMapper bookMapper){ return new BookFacade(bookByIsbnService, authorByKey, bookMapper); }

    @Bean
    public BookMapper bookMapper(){ return new BookMapper(); }
}
