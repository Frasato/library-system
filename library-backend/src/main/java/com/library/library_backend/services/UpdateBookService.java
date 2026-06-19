package com.library.library_backend.services;

import com.library.library_backend.dto.RequestUpdateBookDto;
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
        if(foundedBook.isEmpty()) throw new RuntimeException("Book not found on ID: " + id);

        Book book = foundedBook.get();

        if(!request.titulo().isEmpty()){
            book.setTitulo(request.titulo());
        }

        if(!request.editora().isEmpty()){
            book.setEditora(request.editora());
        }

        if(!request.isbn().isEmpty()){
            book.setIsbn(request.isbn());
        }

        if(!request.dataPublicacao().isEmpty()){
            book.setDataPublicacao(request.dataPublicacao());
        }

        if(!request.livrosSemelhantes().isEmpty()){
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
