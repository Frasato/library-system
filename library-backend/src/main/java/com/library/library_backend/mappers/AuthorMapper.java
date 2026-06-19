package com.library.library_backend.mappers;

import com.library.library_backend.dto.AuthorResponseOpenLibraryDto;
import com.library.library_backend.models.Author;

public class AuthorMapper {

    public Author toEntity(AuthorResponseOpenLibraryDto response){
        Author author = new Author();
        author.setNome(response.name());
        author.setDataNasc(response.birth_date());
        return author;
    }

}