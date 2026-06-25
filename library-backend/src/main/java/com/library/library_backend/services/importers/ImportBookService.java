package com.library.library_backend.services.importers;

import com.library.library_backend.dto.ResponseImportBookDto;
import com.library.library_backend.exceptions.InvalidFileException;
import com.library.library_backend.exceptions.InvalidFileNameException;
import com.library.library_backend.models.Book;
import com.library.library_backend.repositories.BookRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
     *   <li>Valida o arquivo e extrai a sua extensão</li>
     *   <li>Seleciona o importador adequado via {@link ImporterBookFactory}</li>
     *   <li>Converte o arquivo numa lista de {@link Book}</li>
     *   <li>Para cada livro:
     *     <ul>
     *       <li>Se o ISBN já existir no banco — <b>atualiza</b> o registro</li>
     *       <li>Se o ISBN não existir — <b>insere</b> um novo registro</li>
     *     </ul>
     *   </li>
     *   <li>Persiste todos os livros em lote via {@code saveAll}</li>
     * </ol>
     *
     * <p><b>Cache:</b> após a conclusão da importação, todas as entradas
     * do cache {@code books} são invalidadas para garantir que futuras
     * consultas retornem dados atualizados.</p>
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
    @CacheEvict(value = "books", allEntries = true)
    public ResponseImportBookDto importFile(MultipartFile file) {
        if (file == null) throw new InvalidFileException();

        String filename = file.getOriginalFilename();
        if (filename == null) throw new InvalidFileNameException();

        String extension = filename.substring(filename.lastIndexOf(".") + 1);

        BookImporter importer = importerBookFactory.getImporter(extension);
        List<Book> importedBooks = importer.importFile(file);

        int updatedRows = 0;
        int addedRows = 0;

        List<Book> booksToSave = new ArrayList<>();

        for(Book imported : importedBooks){
            Optional<Book> persistedBook = bookRepository.findByIsbnList(imported.getIsbn());
            if(persistedBook.isPresent()){
                Book existingBook = persistedBook.get();
                existingBook.setTitulo(imported.getTitulo());
                existingBook.setIsbn(imported.getIsbn());
                existingBook.setEditora(imported.getEditora());
                existingBook.setDataPublicacao(imported.getDataPublicacao());
                existingBook.setLivrosSemelhantes(imported.getLivrosSemelhantes());
                existingBook.setAuthor(imported.getAuthor());
                updatedRows++;
                booksToSave.add(existingBook);
            }else{
                imported.setId(null);
                addedRows++;
                booksToSave.add(imported);
            }
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