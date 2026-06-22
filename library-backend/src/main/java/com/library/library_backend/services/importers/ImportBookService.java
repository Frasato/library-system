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

    public ResponseImportBookDto importFile(MultipartFile file) {
        if (file == null)
            throw new RuntimeException("File is null");

        String filename = file.getOriginalFilename();

        if (filename == null)
            throw new RuntimeException("File name is not valid!");

        String extension = filename.substring(filename.lastIndexOf(".") + 1);

        BookImporter importer = importerBookFactory.getImporter(extension);
        List<Book> importedBooks = importer.importFile(file);

        AtomicInteger updatedRows = new AtomicInteger();
        AtomicInteger addedRows = new AtomicInteger();

        List<Book> booksToSave = new ArrayList<>();

        for (Book importedBook : importedBooks) {

            Book book = bookRepository
                    .findByIsbnList(importedBook.getIsbn())
                    .map(existingBook -> {
                        existingBook.setTitulo(importedBook.getTitulo());
                        existingBook.setIsbn(importedBook.getIsbn());
                        existingBook.setEditora(importedBook.getEditora());
                        existingBook.setDataPublicacao(importedBook.getDataPublicacao());
                        existingBook.setLivrosSemelhantes(importedBook.getLivrosSemelhantes());
                        existingBook.setAuthor(importedBook.getAuthor());

                        updatedRows.incrementAndGet();

                        return existingBook;
                    })
                    .orElseGet(() -> {
                        importedBook.setId(null);
                        addedRows.incrementAndGet();
                        return importedBook;
                    });

            booksToSave.add(book);
        }

        bookRepository.saveAll(booksToSave);

        return new ResponseImportBookDto(
                HttpStatus.OK,
                updatedRows,
                addedRows,
                Instant.now()
        );
    }
}
