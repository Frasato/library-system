package com.library.library_backend.services.importers;

import com.library.library_backend.exceptions.UnsupportedFileException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImporterBookFactory {
    private final List<BookImporter> importerList;

    public ImporterBookFactory(List<BookImporter> importerList) {
        this.importerList = importerList;
    }

    public BookImporter getImporter(String extension){
        return importerList
                .stream()
                .filter(importer -> importer.supports(extension))
                .findFirst()
                .orElseThrow(UnsupportedFileException::new);
    }
}
