package com.library.library_backend;

import com.library.library_backend.dto.RequestUpdateBookDto;
import com.library.library_backend.dto.ResponseUpdateBookDto;
import com.library.library_backend.exceptions.BookNotFoundException;
import com.library.library_backend.models.Author;
import com.library.library_backend.models.Book;
import com.library.library_backend.repositories.AuthorRepository;
import com.library.library_backend.repositories.BookRepository;
import com.library.library_backend.services.UpdateBookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateBookService — testes unitários")
public class UpdateBookTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @InjectMocks
    private UpdateBookService updateBookService;

    @Test
    @DisplayName("deve lançar BookNotFoundException quando o livro não for encontrado")
    void shouldThrowBookNotFoundExceptionWhenBookDoesNotExist() {
        UUID id = UUID.randomUUID();
        RequestUpdateBookDto request = buildRequest(null, null, null, null, null, null);

        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> updateBookService.updateBook(request, id));
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("deve atualizar apenas o título quando somente ele for informado")
    void shouldUpdateOnlyTituloWhenOnlyTituloIsProvided() {
        UUID id = UUID.randomUUID();
        Book book = buildBook(id, "Título Antigo", List.of("ISBN-000"), List.of("Editora A"), "2020");

        RequestUpdateBookDto request = buildRequest("Título Novo", null, null, null, null, null);

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(any())).thenReturn(book);

        updateBookService.updateBook(request, id);

        assertEquals("Título Novo", book.getTitulo());
        assertEquals(List.of("ISBN-000"), book.getIsbn());
        assertEquals(List.of("Editora A"), book.getEditora());
        assertEquals("2020", book.getDataPublicacao());
    }

    @Test
    @DisplayName("deve atualizar apenas a editora quando somente ela for informada")
    void shouldUpdateOnlyEditoraWhenOnlyEditoraIsProvided() {
        UUID id = UUID.randomUUID();
        Book book = buildBook(id, "Título", List.of("ISBN-000"), List.of("Editora Velha"), "2020");

        RequestUpdateBookDto request = buildRequest(null, List.of("Editora Nova"), null, null, null, null);

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(any())).thenReturn(book);

        updateBookService.updateBook(request, id);

        assertEquals(List.of("Editora Nova"), book.getEditora());
        assertEquals("Título", book.getTitulo());
    }

    @Test
    @DisplayName("deve adicionar ISBN novo quando ele ainda não existir na lista")
    void shouldUpdateOnlyIsbnWhenOnlyIsbnIsProvided() {
        UUID id = UUID.randomUUID();
        Book book = buildBook(id, "Título", List.of("ISBN-000"), List.of("Editora"), "2020");

        RequestUpdateBookDto request = buildRequest(null, null, List.of("ISBN-999"), null, null, null);

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(any())).thenReturn(book);

        updateBookService.updateBook(request, id);

        // Serviço faz merge, não replace — ambos os ISBNs devem estar presentes
        assertTrue(book.getIsbn().containsAll(List.of("ISBN-000", "ISBN-999")));
        assertEquals(2, book.getIsbn().size());
        assertEquals("Título", book.getTitulo());
    }

    @Test
    @DisplayName("deve atualizar apenas a data de publicação quando somente ela for informada")
    void shouldUpdateOnlyDataPublicacaoWhenOnlyDataIsProvided() {
        UUID id = UUID.randomUUID();
        Book book = buildBook(id, "Título", List.of("ISBN-000"), List.of("Editora"), "2020");

        RequestUpdateBookDto request = buildRequest(null, null, null, "2025", null,null);

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(any())).thenReturn(book);

        updateBookService.updateBook(request, id);

        assertEquals("2025", book.getDataPublicacao());
        assertEquals("Título", book.getTitulo());
    }

    @Test
    @DisplayName("deve associar livros semelhantes quando os IDs existirem no banco")
    void shouldAssociateLivrosSemelhantesWhenIdsExist() {
        UUID id = UUID.randomUUID();
        UUID similarId = UUID.randomUUID();

        Book book = buildBook(id, "Título", List.of("ISBN-000"), List.of("Editora"), "2020");
        Book similar = buildBook(similarId, "Livro Similar", List.of("ISBN-111"), List.of("Editora B"), "2021");

        RequestUpdateBookDto request = buildRequest(null, null, null, null, null, List.of(similarId.toString()));

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.findById(similarId)).thenReturn(Optional.of(similar));
        when(bookRepository.save(any())).thenReturn(book);

        updateBookService.updateBook(request, id);

        assertEquals(1, book.getLivrosSemelhantes().size());
        assertEquals(similarId, book.getLivrosSemelhantes().getFirst().getId());
    }

    @Test
    @DisplayName("deve lançar RuntimeException quando um ID de livro semelhante não existir")
    void shouldThrowRuntimeExceptionWhenSimilarBookIdDoesNotExist() {
        UUID id = UUID.randomUUID();
        UUID badId = UUID.randomUUID();
        Book book = buildBook(id, "Título", List.of("ISBN-000"), List.of("Editora"), "2020");

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        RequestUpdateBookDto request = buildRequest(null, null, null, null, null, List.of(badId.toString()));

        assertThrows(RuntimeException.class, () -> updateBookService.updateBook(request, id));

        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("deve salvar sem alterar nenhum campo quando o request estiver vazio")
    void shouldSaveWithoutChangingFieldsWhenRequestIsEmpty() {
        UUID id = UUID.randomUUID();
        Book book = buildBook(id, "Título Original", List.of("ISBN-000"), List.of("Editora A"), "2020");

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        RequestUpdateBookDto request = buildRequest(null, null, null, null, null, null);

        updateBookService.updateBook(request, id);

        assertEquals("Título Original", book.getTitulo());
        assertEquals(List.of("ISBN-000"), book.getIsbn());
        assertEquals(List.of("Editora A"), book.getEditora());
        assertEquals("2020", book.getDataPublicacao());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("deve atualizar todos os campos quando todos forem informados")
    void shouldUpdateAllFieldsWhenAllAreProvided() {
        UUID id = UUID.randomUUID();
        UUID similarId = UUID.randomUUID();

        Book book = buildBook(id, "Título Velho", List.of("ISBN-000"), List.of("Editora A"), "2010");
        Book similar = buildBook(similarId, "Similar", List.of("ISBN-SIM"), List.of("Editora S"), "2015");

        Author autor = new Author();
        autor.setNome("Autor B");

        RequestUpdateBookDto request = buildRequest(
                "Título Novo", List.of("Editora B"), List.of("ISBN-999"),
                "2025", List.of("Autor B"), List.of(similarId.toString())
        );

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.findById(similarId)).thenReturn(Optional.of(similar));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(authorRepository.findAuthorByNome("Autor B")).thenReturn(Optional.of(autor));

        updateBookService.updateBook(request, id);

        assertEquals("Título Novo", book.getTitulo());
        assertEquals(List.of("Editora B"), book.getEditora());
        assertTrue(book.getIsbn().contains("ISBN-999"));
        assertEquals("2025", book.getDataPublicacao());
        assertEquals(1, book.getLivrosSemelhantes().size());
    }

    @Test
    @DisplayName("deve retornar ResponseUpdateBookDto com status 200 e o ID correto")
    void shouldReturnCorrectResponseDto() {
        UUID id = UUID.randomUUID();
        Book book = buildBook(id, "Título", List.of("ISBN-000"), List.of("Editora"), "2020");

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        RequestUpdateBookDto request = buildRequest(null, null, null, null, null, null);

        ResponseUpdateBookDto response = updateBookService.updateBook(request, id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.status());
        assertEquals(id, response.bookId());
        assertNotNull(response.time());
    }

    // Métodos utilitarios para os testes ----------------------------

    // Metodo utilitario para construção do DTO para atualizar um livro
    private RequestUpdateBookDto buildRequest(
            String titulo,
            List<String> editora,
            List<String> isbn,
            String dataPublicacao,
            List<String> autores,
            List<String> livrosSemelhantes
    ) {
        return new RequestUpdateBookDto(titulo, dataPublicacao, isbn, editora, autores, livrosSemelhantes);
    }

    // Metodo utilitario para construção do Book
    private Book buildBook(UUID id, String titulo, List<String> isbn, List<String> editora, String dataPublicacao) {
        return new Book(
                id,
                titulo,
                dataPublicacao,
                new ArrayList<>(isbn),
                new ArrayList<>(editora),
                null,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

}
