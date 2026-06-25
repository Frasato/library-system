package com.library.library_backend;

import com.library.library_backend.models.Author;
import com.library.library_backend.models.Book;
import com.library.library_backend.repositories.AuthorRepository;
import com.library.library_backend.repositories.BookRepository;
import com.library.library_backend.services.CreateBookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateBookService — testes unitários")
class CreateBookTest {

    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private CreateBookService createBookService;

    @Test
    @DisplayName("deve salvar livro quando lista de autores for null")
    void shouldSaveBookWhenAuthorsIsNull() {
        Book book = buildBook(null);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = createBookService.createBook(book);

        assertNotNull(result);
        assertEquals(book.getIsbn(), result.getIsbn());

        verifyNoInteractions(authorRepository);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("deve inserir autor novo quando a key não existe no banco")
    void shouldInsertNewAuthorWhenKeyDoesNotExist() {
        Author author = buildAuthor("OL000001A", "Autor Novo");
        Book book = buildBook(List.of(author));

        when(authorRepository.findAuthorByKey("OL000001A")).thenReturn(Optional.empty());
        when(authorRepository.save(author)).thenReturn(author);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = createBookService.createBook(book);

        assertNotNull(result);
        verify(authorRepository).findAuthorByKey("OL000001A");
        verify(authorRepository).save(author);
        verify(bookRepository).save(book);
    }

    @Test
    @DisplayName("deve reutilizar autor existente quando a key já está no banco")
    void shouldReuseExistingAuthorWhenKeyAlreadyExists() {
        Author authorIncoming = buildAuthor("OL000002A", "Autor Duplicado");
        Author authorFromDb = buildAuthor("OL000002A", "Autor Já Persistido");
        authorFromDb.setId(UUID.randomUUID());

        Book book = buildBook(List.of(authorIncoming));

        when(authorRepository.findAuthorByKey("OL000002A")).thenReturn(Optional.of(authorFromDb));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        createBookService.createBook(book);

        verify(authorRepository, never()).save(any(Author.class));
        verify(authorRepository).findAuthorByKey("OL000002A");

        assertEquals(authorFromDb, book.getAuthor().getFirst());
    }

    @Test
    @DisplayName("deve processar múltiplos autores corretamente (upsert)")
    void shouldHandleMultipleAuthorsWithUpsertLogic() {
        Author autorNovo = buildAuthor("OL000003A", "Autor Novo");
        Author autorExistente = buildAuthor("OL000004A", "Autor Existente");
        Author autorExistenteDb = buildAuthor("OL000004A", "Autor Existente DB");

        Book book = buildBook(new ArrayList<>(List.of(autorNovo, autorExistente)));

        when(authorRepository.findAuthorByKey("OL000003A")).thenReturn(Optional.empty());
        when(authorRepository.findAuthorByKey("OL000004A")).thenReturn(Optional.of(autorExistenteDb));
        when(authorRepository.save(autorNovo)).thenReturn(autorNovo);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = createBookService.createBook(book);

        assertNotNull(result);

        verify(authorRepository, times(1)).save(autorNovo);
        verify(authorRepository, never()).save(autorExistente);

        verify(authorRepository).findAuthorByKey("OL000003A");
        verify(authorRepository).findAuthorByKey("OL000004A");
    }

    @Test
    @DisplayName("deve salvar livro sem interagir com authorRepository quando lista de autores está vazia")
    void shouldSaveBookWhenAuthorsListIsEmpty() {
        Book book = buildBook(new ArrayList<>());
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = createBookService.createBook(book);

        assertNotNull(result);
        verifyNoInteractions(authorRepository);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("deve retornar exatamente o livro retornado pelo bookRepository.save")
    void shouldReturnBookReturnedByRepository() {
        Book input = buildBook(null);
        Book saved = buildBook(null);
        saved.setId(UUID.randomUUID());

        when(bookRepository.save(input)).thenReturn(saved);
        Book result = createBookService.createBook(input);

        assertEquals(saved.getId(), result.getId());
    }

    // Métodos utilitarios para os testes ----------------------------

    // Metodo utilitario para construção do Book
    private Book buildBook(List<Author> authors) {
        return new Book(
                UUID.randomUUID(),
                "Livro de Teste",
                "01 de Janeiro de 2020",
                List.of("9780000000000"),
                List.of("Editora Teste"),
                "01 de Janeiro de 2024",
                null,
                authors
        );
    }

    // Metodo utilitario para construção do Author
    private Author buildAuthor(String key, String nome) {
        Author author = new Author();
        author.setKey(key);
        author.setNome(nome);
        return author;
    }
}