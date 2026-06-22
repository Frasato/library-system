package com.library.library_backend.controllers;

import com.library.library_backend.dto.*;
import com.library.library_backend.models.Book;
import com.library.library_backend.services.BookFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

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

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateBook(@RequestBody RequestUpdateBookDto requestUpdateBookDto, @PathVariable("id") String id){
        return ResponseEntity.status(HttpStatus.OK).body(bookFacade.updateBook(requestUpdateBookDto, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDeleteBookDto> deleteBook(@PathVariable("id") RequestDeleteBookDto requestDeleteBookDto){
        ResponseDeleteBookDto response = bookFacade.deleteBook(requestDeleteBookDto.id());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}