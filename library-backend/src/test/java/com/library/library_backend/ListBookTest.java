package com.library.library_backend;

import com.library.library_backend.models.Book;
import com.library.library_backend.repositories.BookRepository;
import com.library.library_backend.services.AllBooksService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AllBooksService — testes unitários")
class ListBookTest {

    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private AllBooksService allBooksService;

    @Test
    @DisplayName("deve retornar lista com todos os livros quando houver registros")
    void shouldReturnAllBooksWhenRepositoryHasRecords() {
        List<Book> books = List.of(buildBook("Livro A"), buildBook("Livro B"));
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = allBooksService.fetchAllBooks();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("deve retornar lista vazia quando não houver registros")
    void shouldReturnEmptyListWhenRepositoryHasNoRecords() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        List<Book> result = allBooksService.fetchAllBooks();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("deve retornar exatamente o que o repositório devolver, sem transformações")
    void shouldReturnExactlyWhatRepositoryReturns() {
        Book book = buildBook("Livro Exato");
        List<Book> expected = List.of(book);
        when(bookRepository.findAll()).thenReturn(expected);

        List<Book> result = allBooksService.fetchAllBooks();

        assertSame(expected, result);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("deve chamar bookRepository.findAll exatamente uma vez")
    void shouldCallFindAllExactlyOnce() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        allBooksService.fetchAllBooks();

        verify(bookRepository, times(1)).findAll();
        verifyNoMoreInteractions(bookRepository);
    }

    // Metodo utilitario para construção do Book
    private Book buildBook(String titulo) {
        return new Book(
                UUID.randomUUID(),
                titulo,
                "01 de Janeiro de 2020",
                List.of("9780000000000"),
                List.of("Editora Teste"),
                "01 de Janeiro de 2024",
                null,
                List.of()
        );
    }
}