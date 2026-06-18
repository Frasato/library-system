package com.library.library_backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private String key;
    @JsonBackReference
    @ManyToMany(mappedBy = "author")
    private List<Book> livro;

    public Author() {}

    public Author(UUID id, String nome, String dataNasc, String bio, String key, List<Book> livro) {
        this.id = id;
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.bio = bio;
        this.key = key;
        this.livro = livro;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(String dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getKey() {return key;}

    public void setKey(String key) {this.key = key;}

    public List<Book> getLivro() {
        return livro;
    }

    public void setLivro(List<Book> livro) {
        this.livro = livro;
    }
}
