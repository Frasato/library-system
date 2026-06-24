package com.library.library_backend.services;

import com.library.library_backend.dto.*;
import com.library.library_backend.mappers.BookMapper;
import com.library.library_backend.models.Author;
import com.library.library_backend.models.Book;
import com.library.library_backend.services.importers.ImportBookService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

/**
 * Facade responsável por centralizar e coordenar as operações de negócio relacionadas a livros.
 *
 * <p>Atua como ponto único de entrada para o {@link com.library.library_backend.controllers.BookController},
 * orquestrando os serviços especializados para busca, criação, atualização, exclusão e importação de livros.</p>
 *
 * <p>Serviços coordenados:</p>
 * <ul>
 *   <li>{@link BookByIsbnService} — busca de livros na OpenLibrary pelo ISBN</li>
 *   <li>{@link AuthorByKey} — busca de autores na OpenLibrary pela Key</li>
 *   <li>{@link CreateBookService} — persistência de novos livros</li>
 *   <li>{@link AllBooksService} — listagem de livros</li>
 *   <li>{@link UpdateBookService} — atualização de livros</li>
 *   <li>{@link DeleteBookService} — exclusão de livros</li>
 *   <li>{@link ImportBookService} — importação de livros via arquivo</li>
 * </ul>
 *
 * @version 1.0
 * @see com.library.library_backend.controllers.BookController
 */
@Service
public class BookFacade {

    private final BookByIsbnService bookByIsbnService;
    private final AuthorByKey authorByKey;
    private final BookMapper bookMapper;
    private final CreateBookService createBookService;
    private final AllBooksService allBooksService;
    private final DeleteBookService deleteBookService;
    private final UpdateBookService updateBookService;
    private final ImportBookService importBookService;

    public BookFacade(BookByIsbnService bookByIsbnService, AuthorByKey authorByKey, BookMapper bookMapper, CreateBookService createBookService, AllBooksService allBooksService, DeleteBookService deleteBookService, UpdateBookService updateBookService, ImportBookService importBookService) {
        this.bookByIsbnService = bookByIsbnService;
        this.authorByKey = authorByKey;
        this.bookMapper = bookMapper;
        this.createBookService = createBookService;
        this.allBooksService = allBooksService;
        this.deleteBookService = deleteBookService;
        this.updateBookService = updateBookService;
        this.importBookService = importBookService;
    }

    /**
     * Busca as informações de um livro na OpenLibrary pelo ISBN, incluindo seus autores.
     *
     * <p>Fluxo executado:</p>
     * <ol>
     *   <li>Busca os dados do livro na OpenLibrary via {@link BookByIsbnService}</li>
     *   <li>Converte a resposta para a entidade {@link Book} via {@link BookMapper}</li>
     *   <li>Se houver autores, busca cada um pelo sua Key via {@link AuthorByKey}</li>
     *   <li>Retorna o livro mapeado como {@link ResponseBookDto}</li>
     * </ol>
     *
     * @param isbn código ISBN do livro a ser buscado.
     * @return {@link ResponseBookDto} com os dados do livro e seus autores.
     * @throws com.library.library_backend.exceptions.InvalidISBNException caso o ISBN não seja válido.
     * @throws com.library.library_backend.exceptions.BookNotFoundException caso nenhum livro seja encontrado.
     * @throws com.library.library_backend.exceptions.ExternalAPIException caso ocorra erro na OpenLibrary.
     */
    public ResponseBookDto getInfosByIsbn(String isbn) {
        BookResponseOpenLibraryDto responseBook = bookByIsbnService.fetch(isbn);
        Book book = bookMapper.toEntity(responseBook);

        if (responseBook.authors() != null) {
            List<Author> authors = responseBook.authors()
                    .stream()
                    .map(authorKey -> authorByKey.fetch(authorKey.key()))
                    .toList();
            book.setAuthor(authors);
        }

        return bookMapper.toDto(book);
    }

    /**
     * Cria e persiste um novo livro buscando suas informações na OpenLibrary pelo ISBN.
     *
     * <p>Fluxo executado:</p>
     * <ol>
     *   <li>Busca os dados do livro na OpenLibrary via {@link BookByIsbnService}</li>
     *   <li>Converte a resposta para a entidade {@link Book} via {@link BookMapper}</li>
     *   <li>Se houver autores, busca cada um pela sua Key via {@link AuthorByKey}</li>
     *   <li>Persiste o livro via {@link CreateBookService}</li>
     *   <li>Retorna o livro salvo mapeado como {@link ResponseBookDto}</li>
     * </ol>
     *
     * @param isbn código ISBN do livro a ser criado.
     * @return {@link ResponseBookDto} com os dados do livro criado e seus autores.
     * @throws com.library.library_backend.exceptions.InvalidISBNException caso o ISBN não seja válido.
     * @throws com.library.library_backend.exceptions.BookNotFoundException caso nenhum livro seja encontrado.
     * @throws com.library.library_backend.exceptions.ExternalAPIException caso ocorra erro na OpenLibrary.
     * @throws com.library.library_backend.exceptions.InvalidKeyException caso a key de um autor seja inválida.
     * @throws com.library.library_backend.exceptions.AuthorNotFoundException caso um autor não seja encontrado.
     */
    public ResponseBookDto createNewBook(String isbn) {
        BookResponseOpenLibraryDto responseBook = bookByIsbnService.fetch(isbn);
        Book book = bookMapper.toEntity(responseBook);

        if (responseBook.authors() != null) {
            List<Author> authors = responseBook.authors()
                    .stream()
                    .map(authorKey -> authorByKey.fetch(authorKey.key()))
                    .toList();
            book.setAuthor(authors);
        }

        Book savedBook = createBookService.createBook(book);
        return bookMapper.toDto(savedBook);
    }

    /**
     * Delega a importação de livros a partir de um arquivo para o {@link ImportBookService}.
     *
     * <p>Formatos suportados: CSV, TXT e XML.</p>
     *
     * @param file arquivo multipart contendo os dados dos livros a serem importados.
     * @return {@link ResponseImportBookDto} com o resumo da importação (total, sucesso, falhas).
     * @throws com.library.library_backend.exceptions.InvalidFileException caso nenhum arquivo seja informado.
     * @throws com.library.library_backend.exceptions.InvalidFileNameException caso o nome do arquivo seja vazio.
     * @throws com.library.library_backend.exceptions.UnsupportedFileException caso o formato não seja suportado.
     * @throws com.library.library_backend.exceptions.ConvertFileException caso ocorra falha na conversão do arquivo.
     */
    public ResponseImportBookDto importBook(MultipartFile file) {
        return importBookService.importFile(file);
    }

    /**
     * Delega a atualização parcial de um livro para o {@link UpdateBookService}.
     *
     * @param requestUpdateBookDto DTO contendo os campos a serem atualizados.
     * @param id identificador único do livro em formato {@link String}, convertido internamente para {@link UUID}.
     * @return {@link ResponseUpdateBookDto} com o resumo da atualização (status, id, data).
     * @throws IllegalArgumentException caso o {@code id} não seja um UUID válido.
     * @throws com.library.library_backend.exceptions.BookNotFoundException caso nenhum livro seja encontrado.
     */
    public ResponseUpdateBookDto updateBook(RequestUpdateBookDto requestUpdateBookDto, String id) {
        return updateBookService.updateBook(requestUpdateBookDto, UUID.fromString(id));
    }

    /**
     * Retorna todos os livros cadastrados no sistema.
     *
     * @return lista de {@link Book} com todos os livros. Retorna lista vazia caso não haja registros.
     */
    public List<Book> allBooks() {
        return allBooksService.fetchAllBooks();
    }

    /**
     * Delega a exclusão de um livro pelo ID para o {@link DeleteBookService}.
     *
     * @param id identificador único do livro a ser removido.
     * @return {@link ResponseDeleteBookDto} com o resumo da exclusão (id, status, data).
     * @throws com.library.library_backend.exceptions.InvalidIDException caso o ID informado não seja válido.
     * @throws com.library.library_backend.exceptions.BookNotFoundException caso nenhum livro seja encontrado.
     */
    public ResponseDeleteBookDto deleteBook(String id) {
        return deleteBookService.removeBook(id);
    }
}