package com.library.library_backend;

import com.library.library_backend.dto.ResponseImportBookDto;
import com.library.library_backend.exceptions.InvalidFileException;
import com.library.library_backend.exceptions.InvalidFileNameException;
import com.library.library_backend.models.Book;
import com.library.library_backend.repositories.BookRepository;
import com.library.library_backend.services.importers.BookImporter;
import com.library.library_backend.services.importers.ImportBookService;
import com.library.library_backend.services.importers.ImporterBookFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ImportBookService — testes unitários")
public class ImportBookTest {

    @Mock
    private ImporterBookFactory importerBookFactory;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookImporter bookImporter;
    @InjectMocks
    private ImportBookService importBookService;

    @Test
    @DisplayName("deve lançar InvalidFileException quando o arquivo for null")
    void shouldThrowInvalidFileExceptionWhenFileIsNull() {
        assertThrows(InvalidFileException.class, () -> importBookService.importFile(null));

        verifyNoInteractions(importerBookFactory, bookRepository);
    }

    @Test
    @DisplayName("deve lançar InvalidFileNameException quando o nome do arquivo for null")
    void shouldThrowInvalidFileNameExceptionWhenFilenameIsNull() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn(null);

        assertThrows(InvalidFileNameException.class, () -> importBookService.importFile(file));

        verifyNoInteractions(importerBookFactory, bookRepository);
    }

    @Test
    @DisplayName("deve inserir livros novos e retornar addedRows correto")
    void shouldInsertNewBooksAndReturnCorrectAddedRows() {
        Book importedBook = buildBook(null, "Livro Novo", List.of("ISBN-001"));
        MockMultipartFile file = buildFile("livros.csv");

        when(importerBookFactory.getImporter("csv")).thenReturn(bookImporter);
        when(bookImporter.importFile(file)).thenReturn(List.of(importedBook));
        when(bookRepository.findByIsbnList(importedBook.getIsbn())).thenReturn(Optional.empty());
        when(bookRepository.saveAll(any())).thenReturn(List.of(importedBook));

        ResponseImportBookDto response = importBookService.importFile(file);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.status());
        assertEquals(1, response.rows_added());
        assertEquals(0, response.rows_updated());

        assertNull(importedBook.getId());
    }

    @Test
    @DisplayName("deve atualizar livros existentes e retornar updatedRows correto")
    void shouldUpdateExistingBooksAndReturnCorrectUpdatedRows() {
        Book importedBook = buildBook(null, "Título Atualizado", List.of("ISBN-002"));
        Book existingBook = buildBook(UUID.randomUUID(), "Título Antigo", List.of("ISBN-002"));
        MockMultipartFile file = buildFile("livros.csv");

        when(importerBookFactory.getImporter("csv")).thenReturn(bookImporter);
        when(bookImporter.importFile(file)).thenReturn(List.of(importedBook));
        when(bookRepository.findByIsbnList(importedBook.getIsbn())).thenReturn(Optional.of(existingBook));
        when(bookRepository.saveAll(any())).thenReturn(List.of(existingBook));

        ResponseImportBookDto response = importBookService.importFile(file);

        assertEquals(0, response.rows_added());
        assertEquals(1, response.rows_updated());

        assertEquals("Título Atualizado", existingBook.getTitulo());
        assertEquals(List.of("ISBN-002"), existingBook.getIsbn());
    }

    @Test
    @DisplayName("deve contabilizar corretamente adições e atualizações num lote misto")
    void shouldCountAdditionsAndUpdatesCorrectlyInMixedBatch() {
        Book novoBook = buildBook(null, "Livro Novo", List.of("ISBN-NEW"));
        Book existenteBook = buildBook(null, "Livro Existente", List.of("ISBN-OLD"));
        Book existenteDb = buildBook(UUID.randomUUID(), "Antigo", List.of("ISBN-OLD"));

        MockMultipartFile file = buildFile("livros.csv");

        when(importerBookFactory.getImporter("csv")).thenReturn(bookImporter);
        when(bookImporter.importFile(file)).thenReturn(List.of(novoBook, existenteBook));
        when(bookRepository.findByIsbnList(novoBook.getIsbn())).thenReturn(Optional.empty());
        when(bookRepository.findByIsbnList(existenteBook.getIsbn())).thenReturn(Optional.of(existenteDb));
        when(bookRepository.saveAll(any())).thenReturn(List.of());

        ResponseImportBookDto response = importBookService.importFile(file);

        assertEquals(1, response.rows_added());
        assertEquals(1, response.rows_updated());
    }

    @Test
    @DisplayName("deve chamar saveAll uma única vez com todos os livros processados")
    void shouldCallSaveAllOnceWithAllBooks() {
        Book livro1 = buildBook(null, "Livro 1", List.of("ISBN-A"));
        Book livro2 = buildBook(null, "Livro 2", List.of("ISBN-B"));
        MockMultipartFile file = buildFile("livros.csv");

        when(importerBookFactory.getImporter("csv")).thenReturn(bookImporter);
        when(bookImporter.importFile(file)).thenReturn(List.of(livro1, livro2));
        when(bookRepository.findByIsbnList(livro1.getIsbn())).thenReturn(Optional.empty());
        when(bookRepository.findByIsbnList(livro2.getIsbn())).thenReturn(Optional.empty());
        when(bookRepository.saveAll(any())).thenReturn(List.of());

        importBookService.importFile(file);

        verify(bookRepository, times(1)).saveAll(argThat(list -> ((List<?>) list).size() == 2));
    }

    @Test
    @DisplayName("deve retornar zeros e chamar saveAll com lista vazia quando o arquivo não tiver livros")
    void shouldReturnZerosWhenImportedFileIsEmpty() {
        MockMultipartFile file = buildFile("livros.csv");

        when(importerBookFactory.getImporter("csv")).thenReturn(bookImporter);
        when(bookImporter.importFile(file)).thenReturn(List.of());
        when(bookRepository.saveAll(any())).thenReturn(List.of());

        ResponseImportBookDto response = importBookService.importFile(file);

        assertEquals(0, response.rows_added());
        assertEquals(0, response.rows_updated());
        verify(bookRepository, never()).findByIsbnList(any());
        verify(bookRepository, times(1)).saveAll(argThat(list -> ((List<?>) list).isEmpty()));
    }

    @Test
    @DisplayName("deve extrair a extensão correta e acionar o importador adequado")
    void shouldExtractExtensionAndDelegateToCorrectImporter() {
        MockMultipartFile csvFile = buildFile("dados.csv");
        MockMultipartFile xmlFile = buildFile("dados.xml");

        when(importerBookFactory.getImporter("csv")).thenReturn(bookImporter);
        when(importerBookFactory.getImporter("xml")).thenReturn(bookImporter);
        when(bookImporter.importFile(any())).thenReturn(List.of());
        when(bookRepository.saveAll(any())).thenReturn(List.of());

        importBookService.importFile(csvFile);
        importBookService.importFile(xmlFile);

        verify(importerBookFactory).getImporter("csv");
        verify(importerBookFactory).getImporter("xml");
    }

    @Test
    @DisplayName("deve sobrescrever todos os campos do livro existente durante o update")
    void shouldOverwriteAllFieldsOfExistingBookDuringUpdate() {
        List<String> novoIsbn = List.of("ISBN-NEW");
        List<String> novaEditora = List.of("Editora Nova");

        Book importedBook = buildBook(null, "Novo Título", novoIsbn);
        importedBook.setEditora(novaEditora);
        importedBook.setDataPublicacao("2025");

        Book existingBook = buildBook(UUID.randomUUID(), "Título Velho", List.of("ISBN-OLD"));
        existingBook.setEditora(List.of("Editora Velha"));
        existingBook.setDataPublicacao("2010");

        MockMultipartFile file = buildFile("livros.csv");

        when(importerBookFactory.getImporter("csv")).thenReturn(bookImporter);
        when(bookImporter.importFile(file)).thenReturn(List.of(importedBook));
        when(bookRepository.findByIsbnList(importedBook.getIsbn())).thenReturn(Optional.of(existingBook));
        when(bookRepository.saveAll(any())).thenReturn(List.of());

        importBookService.importFile(file);

        assertEquals("Novo Título", existingBook.getTitulo());
        assertEquals(novoIsbn, existingBook.getIsbn());
        assertEquals(novaEditora, existingBook.getEditora());
        assertEquals("2025", existingBook.getDataPublicacao());
    }

    @Test
    @DisplayName("deve retornar ResponseImportBookDto com status 200 e data não nula")
    void shouldReturnResponseWithOkStatusAndNonNullDate() {
        MockMultipartFile file = buildFile("livros.csv");

        when(importerBookFactory.getImporter("csv")).thenReturn(bookImporter);
        when(bookImporter.importFile(file)).thenReturn(List.of());
        when(bookRepository.saveAll(any())).thenReturn(List.of());

        ResponseImportBookDto response = importBookService.importFile(file);

        assertEquals(HttpStatus.OK, response.status());
        assertNotNull(response.time());
    }

    // Métodos utilitarios para os testes ----------------------------

    // Metodo utilitario para construção do Book
    private Book buildBook(UUID id, String titulo, List<String> isbn) {
        return new Book(id, titulo, null, isbn, List.of(), null, null, null);
    }

    // Metodo para criar um mock de um arquivo
    private MockMultipartFile buildFile(String filename) {
        return new MockMultipartFile("file", filename, "text/plain", "conteudo".getBytes());
    }
}
