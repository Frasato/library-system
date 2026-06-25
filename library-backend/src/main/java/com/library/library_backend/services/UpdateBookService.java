package com.library.library_backend.services;

import com.library.library_backend.dto.RequestUpdateBookDto;
import com.library.library_backend.dto.ResponseUpdateBookDto;
import com.library.library_backend.exceptions.BookNotFoundException;
import com.library.library_backend.models.Book;
import com.library.library_backend.repositories.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
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

    /**
     * @param bookRepository repositório para busca e persistência dos livros.
     */
    public UpdateBookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Atualiza parcialmente os dados de um livro pelo seu ID.
     *
     * <p>Somente os campos não nulos do {@code request} serão atualizados. Campos nulos
     * são ignorados e mantêm seus valores originais.</p>
     *
     * <p><b>Atenção:</b> para {@code livrosSemelhantes}, cada ID informado é resolvido
     * para uma entidade {@link Book} via repositório antes de ser associado.</p>
     *
     * @param request DTO contendo os campos a serem atualizados. Campos nulos são ignorados.
     * @param id      identificador único do livro a ser atualizado.
     * @return {@link ResponseUpdateBookDto} com status, ID e data da atualização.
     * @throws BookNotFoundException caso nenhum livro seja encontrado com o ID informado.
     * @throws RuntimeException caso algum ID em {@code livrosSemelhantes} não seja encontrado.
     */
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
            book.setIsbn(request.isbn());
        }

        if(request.dataPublicacao() != null){
            book.setDataPublicacao(request.dataPublicacao());
        }

        if(request.livrosSemelhantes() != null){
            List<Book> books = request
                    .livrosSemelhantes()
                    .stream()
                    .map(bookId -> bookRepository.findById(UUID.fromString(bookId)).orElseThrow(RuntimeException::new))
                    .toList();

            book.setLivrosSemelhantes(books);
        }

        bookRepository.save(book);
        return new ResponseUpdateBookDto(
                HttpStatus.OK,
                id,
                Instant.now()
        );
    }
}
