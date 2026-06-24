package com.library.library_backend.controllers;

import com.library.library_backend.dto.*;
import com.library.library_backend.models.Book;
import com.library.library_backend.services.BookFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
* Controller responsável pelo gerenciamento dos livros
*
* <p>Expõe endpoints REST para operações CRUD e importação de livros</p>
* <p>Base URL: {@code /v1/book}</p>
*
* @version 1.0
*/

@RestController
@RequestMapping("/v1/book")
public class BookController {

    @Autowired
    private BookFacade bookFacade;

    /**
    *
    * Retorna todos os livros cadastrados
    *
    * <p>Endpoint: {@code GET /v1/book}</p>
    *
    * @return {@link ResponseEntity} contendo uma lista de {@link Book} com status {@code 200 OK}
     * Retorna uma lista vazia caso não haja livros cadastrados.
    */
    @GetMapping
    public ResponseEntity<List<Book>> allBooks(){
        List<Book> response = bookFacade.allBooks();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Busca as informações de um livro por seu código ISBN
     *
     * <p>Endpoint: {@code GET /v1/book/{isbn}}</p>
     *
     * @param requestIsbnDto DTO contendo o ISBN do livro a ser buscado, extraído como {@code @PathVariable}.
     * @return {@link ResponseEntity} contendo um {@link ResponseBookDto} com os dados do livro e status {@code 200 OK}
     * @throws com.library.library_backend.exceptions.InvalidISBNException caso não seja informado um ISBN.
     * @throws com.library.library_backend.exceptions.BookNotFoundException caso nenhum livro seja encontrado com o ISBN informado.
     * @throws com.library.library_backend.exceptions.ExternalAPIException caso ocorra um erro na API externa da OpenLibrary
     */
    @GetMapping("/{isbn}")
    public ResponseEntity<ResponseBookDto> getByIsbn(@PathVariable("isbn") RequestIsbnDto requestIsbnDto){
        ResponseBookDto response = bookFacade.getInfosByIsbn(requestIsbnDto.isbn());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Cria um novo livro usando a OpenLibrary API
     *
     * <p>Endpoint: {@code POST /v1/book/create}</p>
     *
     * @param requestIsbnDto DTO contendo o ISBN do livro a ser buscado, extraído como {@code @RequestBody}.
     * @return {@link ResponseEntity} contendo um {@link ResponseBookDto} com os dados do livro e status {@code 201 CREATED}
     * @throws com.library.library_backend.exceptions.InvalidISBNException caso não seja informado um ISBN.
     * @throws com.library.library_backend.exceptions.BookNotFoundException caso nenhum livro seja encontrado com o ISBN informado.
     * @throws com.library.library_backend.exceptions.ExternalAPIException caso ocorra um erro na API externa da OpenLibrary
     * @throws com.library.library_backend.exceptions.InvalidKeyException caso uma key de autor não seja informada.
     * @throws com.library.library_backend.exceptions.AuthorNotFoundException caso um autor não seja encontrado com a Key informada.
     */
    @PostMapping("/create")
    public ResponseEntity<ResponseBookDto> createBook(@RequestBody RequestIsbnDto requestIsbnDto){
        ResponseBookDto response = bookFacade.createNewBook(requestIsbnDto.isbn());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Cria ou Atualiza uma lista de livros informados em algum arquivo.
     * <p>Arquivos suportados: CSV, TXT e XML</p>
     *
     * <p>Endpoint: {@code POST /v1/book/import}</p>
     *
     * @param file arquivo multipart contendo os dados dos livros a serem informados {@code @RequestParam}.
     * @return {@link ResponseEntity} contendo um {@link ResponseImportBookDto} com o resumo da importação (total, sucesso, falhas) e status {@code 200 OK}
     * @throws com.library.library_backend.exceptions.InvalidFileException caso nenhum arquivo seja informado.
     * @throws com.library.library_backend.exceptions.InvalidFileNameException caso nome de arquivo seja vazio.
     * @throws com.library.library_backend.exceptions.UnsupportedFileException caso não tenha suporte para o formato do arquivo importado.
     * @throws com.library.library_backend.exceptions.ConvertFileException caso ocorra alguma falha durante a conversão do arquivo informado.
     */
    @PostMapping("/import")
    public ResponseEntity<ResponseImportBookDto> importBook(@RequestParam MultipartFile file){
        return ResponseEntity.status(HttpStatus.OK).body(bookFacade.importBook(file));
    }

    /**
     * Atualizar parcialmente os dados de um livro pelo ID.
     *
     * <p>Endpoint: {@code DELETE /v1/book/delete/{id}}</p>
     *
     * @param requestUpdateBookDto DTO contendo os campos que podem ser atualizados através do {@code @RequestBody}.
     * @param id identificador único do livro, pego através do {@code @PathVariable}
     * @return {@link ResponseEntity} contendo um {@link ResponseUpdateBookDto} com o resumo da atualização (status, id, data) e status {@code 200 OK}
     * @throws com.library.library_backend.exceptions.BookNotFoundException caso nenhum livro seja encontrado com o ID informado.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseUpdateBookDto> updateBook(@RequestBody RequestUpdateBookDto requestUpdateBookDto, @PathVariable("id") String id){
        return ResponseEntity.status(HttpStatus.OK).body(bookFacade.updateBook(requestUpdateBookDto, id));
    }

    /**
     * Remover um livro pelo ID.
     *
     * <p>Endpoint: {@code DELETE /v1/book/delete/{id}}</p>
     *
     * @param requestDeleteBookDto DTO o ID do livro a ser deletado através do {@code @PathVariable}.
     * @return {@link ResponseEntity} contendo um {@link ResponseDeleteBookDto} com o resumo da exclusão (id, status, data) e status {@code 200 OK}
     * @throws com.library.library_backend.exceptions.InvalidIDException caso o ID informado não seja válido.
     * @throws com.library.library_backend.exceptions.BookNotFoundException caso nenhum livro seja encontrado com o ID informado.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDeleteBookDto> deleteBook(@PathVariable("id") RequestDeleteBookDto requestDeleteBookDto){
        ResponseDeleteBookDto response = bookFacade.deleteBook(requestDeleteBookDto.id());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}