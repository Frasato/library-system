package com.library.library_backend.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "autores")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nome;
    private String dataNasc;
    private String bio;
    @ManyToMany(mappedBy = "author")
    private List<Book> livro;

}
