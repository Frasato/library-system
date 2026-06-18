package com.library.library_backend.mappers;

import com.library.library_backend.dto.BookResponseOpenLibraryDto;
import com.library.library_backend.dto.ResponseGetBookIsbDto;
import com.library.library_backend.models.Book;

public class BookMapper {

    public ResponseGetBookIsbDto toDto(Book book){
        return new ResponseGetBookIsbDto(
                book.getTitulo(),
                book.getDataPublicacao(),
                book.getEditora(),
                book.getAuthor()
        );
    }

    public Book toEntity(BookResponseOpenLibraryDto responseOpenLibraryDto){
        Book book = new Book();
        book.setTitulo(responseOpenLibraryDto.title());
        book.setEditora(responseOpenLibraryDto.publishers());
        book.setIsbn(responseOpenLibraryDto.isbn_13());
        book.setDataPublicacao(responseOpenLibraryDto.publish_date());
        return book;
    }
}
