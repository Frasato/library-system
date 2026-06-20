package com.library.library_backend.services.importers;

import com.library.library_backend.models.Book;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookImporter {
    boolean supports(String extension);
    List<Book> importFile(MultipartFile file);
}
