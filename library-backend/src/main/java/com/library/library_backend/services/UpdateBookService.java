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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
            List<String> isbnAtual = new ArrayList<>(book.getIsbn()); // cópia mutável

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
            List<Author> authors = new ArrayList<>(request.autores()
                    .stream()
                    .map(name ->
                            authorRepository
                                    .findAuthorByNome(name)
                                    .orElseGet(() -> {
                                        Author author = new Author();
                                        author.setNome(name);
                                        return authorRepository.save(author);
                                    })
                    )
                    .toList());

            book.setAuthor(authors);
        }

        if(request.livrosSemelhantes() != null){
            List<String> idsSemelhantes = book.getLivrosSemelhantes()
                    .stream()
                    .map(b -> b.getId().toString())
                    .toList();

            List<Book> books = request
                    .livrosSemelhantes()
                    .stream()
                    .filter(bookId -> !bookId.equals(id.toString()))
                    .filter(bookId -> !idsSemelhantes.contains(bookId))
                    .map(bookId -> bookRepository.findById(UUID.fromString(bookId))
                            .orElseThrow(() -> new BookNotFoundException(bookId)))
                    .toList();

            book.getLivrosSemelhantes().addAll(books);
        }

        bookRepository.save(book);
        return new ResponseUpdateBookDto(
                HttpStatus.OK,
                id,
                Instant.now()
        );
    }
}
