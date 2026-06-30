package com.library.library_backend.services;

import com.library.library_backend.dto.RequestUpdateBookDto;
import com.library.library_backend.dto.ResponseUpdateBookDto;
import com.library.library_backend.exceptions.BookNotFoundException;
import com.library.library_backend.models.Author;
import com.library.library_backend.models.Book;
import com.library.library_backend.repositories.AuthorRepository;
import com.library.library_backend.repositories.BookRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service responsável pela atualização parcial de livros.
 *
 * <p>Apenas os campos não nulos do {@link RequestUpdateBookDto} são aplicados,
 * preservando os valores originais dos campos ausentes na requisição.</p>
 *
 * @see RequestUpdateBookDto
 */
@Service
public class UpdateBookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    /**
     * @param bookRepository repositório para busca e persistência dos livros.
     * @param authorRepository repositório para busca e persistência dos autores.
     */
    public UpdateBookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    /**
     * Atualiza parcialmente os dados de um livro pelo seu ID.
     *
     * <p>Somente os campos não nulos do {@code request} serão atualizados. Campos nulos
     * são ignorados e mantêm seus valores originais.</p>
     *
     * <p><b>Cache:</b> após a conclusão da atualização, todas as entradas
     * do cache {@code books} são invalidadas para garantir que futuras
     * consultas retornem dados atualizados.</p>
     *
     * <p><b>Atenção:</b> para {@code livrosSemelhantes}, cada ID informado é resolvido
     * para uma entidade {@link Book} via repositório antes de ser associado.</p>
     *
     * @param request DTO contendo os campos a serem atualizados. Campos nulos são ignorados.
     * @param id      identificador único do livro a ser atualizado.
     * @return {@link ResponseUpdateBookDto} com status, ID e data da atualização.
     * @throws BookNotFoundException caso nenhum livro seja encontrado com o ID informado.
     * @throws BookNotFoundException caso algum ID em {@code livrosSemelhantes} não seja encontrado.
     */
    @CacheEvict(value = "books", allEntries = true)
    public ResponseUpdateBookDto updateBook(RequestUpdateBookDto request, UUID id){
        Optional<Book> foundedBook = bookRepository.findById(id);
        if(foundedBook.isEmpty()) throw new BookNotFoundException(id.toString());

        Book book = foundedBook.get();

        if(request.titulo() != null){
            book.setTitulo(request.titulo());
        }

        if(request.editora() != null){
            book.setEditora(request.editora());
        }

        if(request.isbn() != null){
            List<String> isbnAtual = new ArrayList<>(book.getIsbn());

            List<String> newIsbn = request.isbn()
                    .stream()
                    .map(String::trim)
                    .filter(isbn -> !isbnAtual.contains(isbn))
                    .toList();

            isbnAtual.addAll(newIsbn);
            book.setIsbn(isbnAtual);
        }

        if(request.dataPublicacao() != null){
            book.setDataPublicacao(request.dataPublicacao());
        }

        if(request.autores() != null && !request.autores().isEmpty()){
            List<Author> authorList = new ArrayList<>(authorRepository.findByNomeIn(request.autores()));

            Set<String> existingNames = authorList.stream()
                    .map(Author::getNome)
                    .collect(Collectors.toSet());

            List<Author> newAuthors = request.autores().stream()
                    .filter(nome -> !existingNames.contains(nome))
                    .map(nome -> {
                        Author author = new Author();
                        author.setNome(nome);
                        return author;
                    })
                    .toList();

            List<Author> savedAuthors = authorRepository.saveAll(newAuthors);
            authorList.addAll(savedAuthors);

            book.setAuthor(authorList);
        }

        if(request.livrosSemelhantes() != null){
            List<String> similarIds = book.getLivrosSemelhantes()
                    .stream()
                    .map(b -> b.getId().toString())
                    .toList();

            List<UUID> searchIds = request
                    .livrosSemelhantes()
                    .stream()
                    .filter(bookId -> !similarIds.contains(bookId))
                    .map(UUID::fromString)
                    .toList();

            List<Book> foundBooks = bookRepository.findAllById(searchIds);

            if(foundBooks.size() != searchIds.size()){
                List<String> foundIds = foundBooks
                        .stream()
                        .map(b -> b.getId().toString())
                        .toList();

                UUID idNotFound = searchIds
                        .stream()
                        .filter(bookId -> !foundIds.contains(bookId.toString()))
                        .findFirst()
                        .orElseThrow();

                throw new BookNotFoundException(idNotFound.toString());
            }

            book.getLivrosSemelhantes().addAll(foundBooks);
        }

        bookRepository.save(book);
        return new ResponseUpdateBookDto(
                HttpStatus.OK,
                id,
                Instant.now()
        );
    }
}
