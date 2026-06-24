package com.library.library_backend.services.importers;

import com.library.library_backend.exceptions.ConvertFileException;
import com.library.library_backend.models.Author;
import com.library.library_backend.models.Book;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
public class TextBookImporter implements BookImporter{
    @Override
    public boolean supports(String extension) { return extension.equals("txt"); }

    @Override
    public List<Book> importFile(MultipartFile file) {
        List<Book> books = new ArrayList<>();

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;

            while((line = reader.readLine()) != null){
                String[] values = line.split("\t");

                Book book = new Book();
                book.setTitulo(values[0].trim());
                book.setDataPublicacao(values[1].trim());

                ArrayList<String> isbn = new ArrayList<>();
                isbn.add(values[2].trim());
                book.setIsbn(isbn);

                ArrayList<String> publishers = new ArrayList<>();
                publishers.add(values[3]);
                book.setEditora(publishers);

                List<Author> authors = Stream.of(values[4].split(";"))
                        .map(String::trim)
                        .map(name -> {
                            Author author = new Author();
                            author.setNome(name);
                            return author;
                        })
                        .toList();

                book.setAuthor(authors);
                books.add(book);
            }

            return books;

        }catch(IOException e){
            throw new ConvertFileException(e.getMessage());
        }
    }
}
