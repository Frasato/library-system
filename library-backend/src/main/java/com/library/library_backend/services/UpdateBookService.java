package com.library.library_backend.services;

import com.library.library_backend.dto.RequestUpdateBookDto;
import com.library.library_backend.exceptions.BookNotFoundException;
import com.library.library_backend.models.Book;
import com.library.library_backend.repositories.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UpdateBookService {

    private final BookRepository bookRepository;

    public UpdateBookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void updateBook(RequestUpdateBookDto request, UUID id){
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
    }
}
