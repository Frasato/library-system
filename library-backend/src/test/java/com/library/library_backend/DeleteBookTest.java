package com.library.library_backend;

import com.library.library_backend.dto.ResponseDeleteBookDto;
import com.library.library_backend.exceptions.BookNotFoundException;
import com.library.library_backend.exceptions.InvalidIDException;
import com.library.library_backend.models.Book;
import com.library.library_backend.repositories.BookRepository;
import com.library.library_backend.services.DeleteBookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteBookService — testes unitários")
public class DeleteBookTest {

    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private DeleteBookService deleteBookService;

    @Test
    @DisplayName("deve excluir o livro e retornar ResponseDeleteBookDto com status 'deleted'")
    void shouldDeleteBookAndReturnCorrectResponse() {
        UUID id   = UUID.randomUUID();
        Book book = buildBook(id);

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        ResponseDeleteBookDto response = deleteBookService.removeBook(id.toString());

        assertNotNull(response);
        assertEquals(id.toString(), response.id());
        assertEquals("deleted", response.status());
        assertNotNull(response.deletedAt());

        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    @DisplayName("deve lançar InvalidIDException quando o ID for uma string vazia")
    void shouldThrowInvalidIDExceptionWhenIdIsEmpty() {
        assertThrows(InvalidIDException.class, () -> deleteBookService.removeBook(""));
        verifyNoInteractions(bookRepository);
    }

    @Test
    @DisplayName("deve lançar IllegalArgumentException quando o ID não for um UUID válido")
    void shouldThrowIllegalArgumentExceptionWhenIdIsNotValidUUID() {
        assertThrows(IllegalArgumentException.class, () -> deleteBookService.removeBook("id-invalido-nao-uuid"));
        verifyNoInteractions(bookRepository);
    }

    @Test
    @DisplayName("deve lançar BookNotFoundException quando nenhum livro for encontrado com o ID")
    void shouldThrowBookNotFoundExceptionWhenBookDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> deleteBookService.removeBook(id.toString()));
        verify(bookRepository, never()).delete(any());
    }

    @Test
    @DisplayName("deve chamar bookRepository.delete() com a entidade encontrada, não outra")
    void shouldCallDeleteWithCorrectBookEntity() {
        UUID id   = UUID.randomUUID();
        Book book = buildBook(id);

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        deleteBookService.removeBook(id.toString());

        verify(bookRepository).delete(book);
    }

    @Test
    @DisplayName("não deve interagir com o repositório quando o ID for inválido")
    void shouldNotInteractWithRepositoryWhenIdIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> deleteBookService.removeBook("nao-e-uuid"));
        assertThrows(InvalidIDException.class, () -> deleteBookService.removeBook(""));
        verifyNoInteractions(bookRepository);
    }

    // Metodo utilitario para construção do Book
    private Book buildBook(UUID id) {
        return new Book(
                id,
                "Livro de Teste",
                "01 de Janeiro de 2020",
                List.of("9780000000000"),
                List.of("Editora Teste"),
                "01 de Janeiro de 2024",
                null,
                List.of()
        );
    }
}
