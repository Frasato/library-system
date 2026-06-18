package com.library.library_backend.dto;

import com.library.library_backend.models.Book;

import java.util.List;

public record BookResponseOpenLibraryDto(
        String title,
        List<AuthorKey> authors,
        List<String> publishers,
        List<String> isbn_13,
        Created created
){
    public Book toEntity(){
        Book book = new Book();
        book.setTitulo(this.title);
        book.setEditora(this.publishers);
        book.setIsbn(this.isbn_13);
        book.setCreatedAt(this.created.value());
        return book;
    }
}

record AuthorKey(
        String key
){}

record Created(
        String value
){}
