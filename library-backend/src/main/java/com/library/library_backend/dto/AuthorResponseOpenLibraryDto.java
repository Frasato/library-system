package com.library.library_backend.dto;

import com.library.library_backend.models.Author;

public record AuthorResponseOpenLibraryDto(
        String birth_date,
        String name,
        BioDto bio
){
    public Author toEntity(){
        Author author = new Author();
        author.setNome(this.name);
        author.setDataNasc(this.birth_date);
        author.setBio(this.bio.value());
        return author;
    }
}