package com.library.library_backend.services.importers;

import com.library.library_backend.dto.ResponseImportBookDto;
import com.library.library_backend.exceptions.InvalidFileException;
import com.library.library_backend.exceptions.InvalidFileNameException;
import com.library.library_backend.models.Book;
import com.library.library_backend.repositories.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service responsável por processar a importação de livros a partir de arquivos.
 *
 * <p>Coordena a leitura do arquivo via {@link ImporterBookFactory} e aplica
 * a lógica de <b>upsert</b>: livros já existentes (identificados pelo ISBN)
 * são atualizados, e livros novos são inseridos.</p>
 *
 * @see ImporterBookFactory
 * @see BookImporter
 */
@Service
public class ImportBookService {

    private final ImporterBookFactory importerBookFactory;
    private final BookRepository bookRepository;

    /**
     * @param importerBookFactory factory para seleção do importador adequado ao formato do arquivo.
     * @param bookRepository      repositório para persistência dos livros importados.
     */
    public ImportBookService(ImporterBookFactory importerBookFactory, BookRepository bookRepository) {
        this.importerBookFactory = importerBookFactory;
        this.bookRepository = bookRepository;
    }

    /**
     * Processa um arquivo de importação de livros aplicando lógica de upsert.
     *
     * <p>Fluxo executado:</p>
     * <ol>
     *   <li>Valida o arquivo e extrai sua extensão</li>
     *   <li>Seleciona o importador adequado via {@link ImporterBookFactory}</li>
     *   <li>Converte o arquivo em uma lista de {@link Book}</li>
     *   <li>Para cada livro:
     *     <ul>
     *       <li>Se o ISBN já existir no banco — <b>atualiza</b> o registro</li>
     *       <li>Se o ISBN não existir — <b>insere</b> um novo registro</li>
     *     </ul>
     *   </li>
     *   <li>Persiste todos os livros em lote via {@code saveAll}</li>
     * </ol>
     *
     * @param file arquivo multipart a ser importado (CSV, TXT ou XML).
     * @return {@link ResponseImportBookDto} com o resumo da importação contendo
     *         quantidade de registros adicionados, atualizados e a data de processamento.
     * @throws com.library.library_backend.exceptions.InvalidFileException
     *         caso o arquivo seja {@code null}.
     * @throws com.library.library_backend.exceptions.InvalidFileNameException
     *         caso o nome do arquivo seja {@code null}.
     * @throws com.library.library_backend.exceptions.UnsupportedFileException
     *         caso a extensão do arquivo não seja suportada.
     * @throws com.library.library_backend.exceptions.ConvertFileException
     *         caso ocorra falha na conversão do arquivo.
     */
    public ResponseImportBookDto importFile(MultipartFile file) {
        if (file == null) throw new InvalidFileException();

        String filename = file.getOriginalFilename();
        if (filename == null) throw new InvalidFileNameException();

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