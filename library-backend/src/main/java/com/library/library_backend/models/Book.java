package com.library.library_backend.models;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "livros")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String titulo;
    private Date dataPublicacao;
    private String isbn;
    private String editora;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    @ManyToMany
    @JoinTable(
            name = "livros_semelhantes",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "livro_semelhante_id")
    )
    private List<Book> livrosSemelhantes;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

}
