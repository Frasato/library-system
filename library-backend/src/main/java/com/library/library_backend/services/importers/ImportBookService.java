package com.library.library_backend.services.importers;

import com.library.library_backend.dto.ResponseImportBookDto;
import com.library.library_backend.models.Book;
import com.library.library_backend.repositories.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ImportBookService {

    private final ImporterBookFactory importerBookFactory;
    private final BookRepository bookRepository;

    public ImportBookService(ImporterBookFactory importerBookFactory, BookRepository bookRepository) {
        this.importerBookFactory = importerBookFactory;
        this.bookRepository = bookRepository;
    }

    public ResponseImportBookDto importFile(MultipartFile file){
        if(file == null) throw new RuntimeException("");
        String filename = file.getOriginalFilename();

        if (filename == null) throw new RuntimeException("File name is not valid!");

        String extension = filename.substring(filename.lastIndexOf(".") + 1);

        BookImporter importer = importerBookFactory.getImporter(extension);
        List<Book> books = importer.importFile(file);

        AtomicInteger updatedRows = new AtomicInteger();
        AtomicInteger addedRows = new AtomicInteger();
        List<Book> saveBooks = new ArrayList<>();

        books.forEach(importedBook -> {
            Book book = bookRepository
                    .findByIsbnList(importedBook.getIsbn())
                    .map(foundedBook -> {
                        foundedBook.setTitulo(importedBook.getTitulo());
                        foundedBook.setIsbn(importedBook.getIsbn());
                        foundedBook.setEditora(importedBook.getEditora());
                        foundedBook.setDataPublicacao(importedBook.getDataPublicacao());
                        foundedBook.setLivrosSemelhantes(importedBook.getLivrosSemelhantes());
                        foundedBook.setAuthor(importedBook.getAuthor());
                        updatedRows.getAndIncrement();
                        return foundedBook;
                    }).orElseGet(() -> {
                        addedRows.getAndIncrement();
                        return importedBook;
                    });

            saveBooks.add(book);
        });

        bookRepository.saveAll(saveBooks);

        return new ResponseImportBookDto(
                HttpStatus.OK,
                updatedRows,
                addedRows,
                Instant.now()
        );
    }
}
