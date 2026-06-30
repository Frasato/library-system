package com.library.library_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Entidade que representa um livro no acervo da biblioteca.
 *
 * <p>Mapeada para a tabela {@code livro} no banco de dados.</p>
 */
@Entity
@Table(name = "livro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String titulo;
    private String dataPublicacao;
    @ElementCollection
    private List<String> isbn;
    @ElementCollection
    private List<String> editora;
    private String createdAt;

    /**
     * Lista de livros semelhantes.
     *
     * <p>Relacionamento {@code @ManyToMany} auto-referenciado, mapeado pela
     * tabela {@code livros_semelhantes}.</p>
     *
     * <p>Utilizando o {@code @JsonIgnoreProperties} para evitar
     * loop infinito e acontecer um StackOverflowException.</p>
     */
    @ManyToMany
    @JoinTable(
            name = "livros_semelhantes",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "livro_semelhante_id")
    )
    @JsonIgnoreProperties({"livrosSemelhantes", "author"})
    private List<Book> livrosSemelhantes;

    /**
     * Lista de autores do livro.
     *
     * <p>Relacionamento {@code @ManyToMany} com {@link Author}, mapeado pela tabela {@code livr_autor}.</p>
     * <p>{@code CascadeType.PERSIST} e {@code CascadeType.MERGE} garantem que autores sejam
     * salvos ou atualizados junto com o livro. {@code @JsonManagedReference} evita
     * recursão infinita na serialização JSON.</p>
     */
    @JsonManagedReference
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "livr_autor",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Author> author;

    /**
     * Define automaticamente a data e hora de criação do registro antes de persistir.
     *
     * <p>Executado pelo JPA via {@code @PrePersist} — não deve ser chamado manualmente.</p>
     */
    @PrePersist
    private void setCreatedAt(){
        this.createdAt = Instant.now().toString();
    }
}
