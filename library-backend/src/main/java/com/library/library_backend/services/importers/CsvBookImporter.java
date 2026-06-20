package com.library.library_backend.services.importers;

import com.library.library_backend.models.Book;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvBookImporter implements BookImporter{
    @Override
    public boolean supports(String extension) {
        return extension.equals("csv");
    }

    @Override
    public List<Book> importFile(MultipartFile file) {

        List<Book> books = new ArrayList<>();

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;

            while((line = reader.readLine()) != null){
                String[] values = line.split(",");

                Book book = new Book();
                book.setTitulo(values[0]);
                book.setDataPublicacao(values[1]);
                book.setIsbn(List.of(values[2]));
                book.setEditora(List.of(values[3]));
                books.add(book);
            }

            return books;
        }catch(IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
