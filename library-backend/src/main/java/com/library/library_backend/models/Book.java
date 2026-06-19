package com.library.library_backend.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "livro")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String titulo;
    private String dataPublicacao;
    private List<String> isbn;
    private List<String> editora;
    private String createdAt;

    @ManyToMany
    @JoinTable(
            name = "livros_semelhantes",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "livro_semelhante_id")
    )
    private List<Book> livrosSemelhantes;

    @JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "livr_autor",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Author> author;

    public Book() {}

    public Book(UUID id, String titulo, String dataPublicacao, List<String> isbn, List<String> editora, String createdAt, List<Book> livrosSemelhantes, List<Author> author) {
        this.id = id;
        this.titulo = titulo;
        this.dataPublicacao = dataPublicacao;
        this.isbn = isbn;
        this.editora = editora;
        this.createdAt = createdAt;
        this.livrosSemelhantes = livrosSemelhantes;
        this.author = author;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(String dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public List<String> getIsbn() {
        return isbn;
    }

    public void setIsbn(List<String> isbn) {
        this.isbn = isbn;
    }

    public List<String> getEditora() {
        return editora;
    }

    public void setEditora(List<String> editora) {
        this.editora = editora;
    }

    public String getCreatedAt() {return createdAt;}

    public void setCreatedAt(String createdAt) {this.createdAt = createdAt;}

    public List<Book> getLivrosSemelhantes() {
        return livrosSemelhantes;
    }

    public void setLivrosSemelhantes(List<Book> livrosSemelhantes) {
        this.livrosSemelhantes = livrosSemelhantes;
    }

    public List<Author> getAuthor() {
        return author;
    }

    public void setAuthor(List<Author> author) {
        this.author = author;
    }

    @PrePersist
    private void setCreatedAt(){
        this.createdAt = Instant.now().toString();
    }
}
