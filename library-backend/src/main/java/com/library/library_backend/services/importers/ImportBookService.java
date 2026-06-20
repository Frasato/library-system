package com.library.library_backend.services.importers;

import com.library.library_backend.models.Book;
import com.library.library_backend.repositories.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
public class ImportBookService {

    private final ImporterBookFactory importerBookFactory;
    private final BookRepository bookRepository;

    public ImportBookService(ImporterBookFactory importerBookFactory, BookRepository bookRepository) {
        this.importerBookFactory = importerBookFactory;
        this.bookRepository = bookRepository;
    }

    public void importFile(MultipartFile file){
        if(file == null) throw new RuntimeException("");
        String filename = file.getOriginalFilename();

        assert filename != null;

        String extension = filename.substring(filename.lastIndexOf(".") + 1);

        BookImporter importer = importerBookFactory.getImporter(extension);
        List<Book> books = importer.importFile(file);

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
                        return foundedBook;
                    }).orElse(importedBook);

            bookRepository.save(book);
        });
    }
}
