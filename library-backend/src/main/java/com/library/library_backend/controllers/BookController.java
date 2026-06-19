package com.library.library_backend.controllers;

import com.library.library_backend.dto.RequestDeleteBookDto;
import com.library.library_backend.dto.RequestIsbnDto;
import com.library.library_backend.dto.ResponseBookDto;
import com.library.library_backend.dto.ResponseDeleteBookDto;
import com.library.library_backend.models.Book;
import com.library.library_backend.services.BookFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/book")
public class BookController {

    @Autowired
    private BookFacade bookFacade;

    @GetMapping
    public ResponseEntity<List<Book>> allBooks(){
        List<Book> response = bookFacade.allBooks();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<ResponseBookDto> getByIsbn(@PathVariable("isbn") RequestIsbnDto requestIsbnDto){
        ResponseBookDto response = bookFacade.getInfosByIsbn(requestIsbnDto.isbn());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseBookDto> createBook(@RequestBody RequestIsbnDto requestIsbnDto){
        ResponseBookDto response = bookFacade.createNewBook(requestIsbnDto.isbn());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDeleteBookDto> deleteBook(@PathVariable("id") RequestDeleteBookDto requestDeleteBookDto){
        ResponseDeleteBookDto response = bookFacade.deleteBook(requestDeleteBookDto.id());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}