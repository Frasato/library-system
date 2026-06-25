package com.library.library_backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * Entidade que representa um autor no acervo da biblioteca.
 *
 * <p>Mapeada para a tabela {@code autores} no banco de dados.</p>
 */
@Entity
@Table(name = "autores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nome;
    private String dataNasc;
    private String key;

    /**
     * Lista de livros associados ao autor.
     *
     * <p>Lado inverso do relacionamento {@code @ManyToMany} com {@link Book},
     * controlado pelo campo {@code author} em {@link Book}.</p>
     * <p>{@code @JsonBackReference} evita recursão infinita na serialização JSON,
     * sendo o lado que <b>não</b> é serializado.</p>
     */
    @JsonBackReference
    @ManyToMany(mappedBy = "author")
    private List<Book> livro;
}
