package com.library.library_backend.controllers;

import com.library.library_backend.dto.ResponseGetBookIsbDto;
import com.library.library_backend.mappers.BookMapper;
import com.library.library_backend.models.Book;
import com.library.library_backend.services.BookFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/book")
public class BookController {

    @Autowired
    private BookFacade bookFacade;
    @Autowired
    private BookMapper bookMapper;

    @GetMapping("/{isbn}")
    public ResponseEntity<ResponseGetBookIsbDto> getByIsbn(@PathVariable("isbn") String isbn){
        Book book = bookFacade.getInfosByIsbn(isbn);
        ResponseGetBookIsbDto response = bookMapper.toDto(book);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}