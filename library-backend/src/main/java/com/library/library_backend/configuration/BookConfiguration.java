package com.library.library_backend.configuration;

import com.library.library_backend.mappers.AuthorMapper;
import com.library.library_backend.mappers.BookMapper;
import com.library.library_backend.repositories.AuthorRepository;
import com.library.library_backend.repositories.BookRepository;
import com.library.library_backend.services.*;
import com.library.library_backend.services.importers.ImportBookService;
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
    public AuthorByKey AuthorByKey(RestTemplate restTemplate, AuthorMapper authorMapper){ return new AuthorByKey(restTemplate, authorMapper);}

    @Bean
    public BookFacade bookFacade(BookByIsbnService bookByIsbnService, AuthorByKey authorByKey, BookMapper bookMapper, CreateBookService createBookService, AllBooksService allBooksService, DeleteBookService deleteBookService, UpdateBookService updateBookService, ImportBookService importBookService){
        return new BookFacade(bookByIsbnService, authorByKey, bookMapper, createBookService, allBooksService, deleteBookService, updateBookService, importBookService);
    }

    @Bean
    public BookMapper bookMapper(){ return new BookMapper(); }

    @Bean
    public AuthorMapper authorMapper(){return new AuthorMapper();}

    @Bean
    public CreateBookService createBookService(BookRepository bookRepository, AuthorRepository authorRepository){
        return new CreateBookService(authorRepository, bookRepository);
    }

    @Bean
    public AllBooksService allBooksService(BookRepository repository){
        return new AllBooksService(repository);
    }

    @Bean
    public DeleteBookService deleteBookService(BookRepository bookRepository){
        return new DeleteBookService(bookRepository);
    }
}
