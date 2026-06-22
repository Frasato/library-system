package com.library.library_backend.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

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

    @ManyToMany
    @JoinTable(
            name = "livros_semelhantes",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "livro_semelhante_id")
    )
    private List<Book> livrosSemelhantes;

    @JsonManagedReference
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "livr_autor",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Author> author;

    @PrePersist
    private void setCreatedAt(){
        this.createdAt = Instant.now().toString();
    }
}
