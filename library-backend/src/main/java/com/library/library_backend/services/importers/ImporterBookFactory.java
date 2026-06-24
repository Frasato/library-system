package com.library.library_backend.services.importers;

import com.library.library_backend.exceptions.UnsupportedFileException;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Factory responsável por selecionar o importador adequado com base na extensão do arquivo.
 *
 * <p>Utiliza o padrão de projeto <b>Factory</b> combinado com injeção automática de todas
 * as implementações de {@link BookImporter} disponíveis no contexto do Spring.</p>
 *
 * <p>Para adicionar suporte a um novo formato, basta criar uma classe que implemente
 * {@link BookImporter} e anotá-la com {@code @Component} — ela será registrada automaticamente.</p>
 *
 * @see BookImporter
 */
@Component
public class ImporterBookFactory {

    private final List<BookImporter> importerList;

    /**
     * Injeta automaticamente todas as implementações de {@link BookImporter}
     * registradas no contexto do Spring.
     *
     * @param importerList lista de importadores disponíveis.
     */
    public ImporterBookFactory(List<BookImporter> importerList) {
        this.importerList = importerList;
    }

    /**
     * Retorna o importador compatível com a extensão informada.
     *
     * @param extension extensão do arquivo (ex: {@code "csv"}, {@code "txt"}, {@code "xml"}).
     * @return {@link BookImporter} compatível com a extensão informada.
     * @throws com.library.library_backend.exceptions.UnsupportedFileException caso nenhum importador suporte a extensão informada.
     * @throws com.library.library_backend.exceptions.ConvertFileException caso ocorra um erro ao converter um arquivo.
     */
    public BookImporter getImporter(String extension) {
        return importerList
                .stream()
                .filter(importer -> importer.supports(extension))
                .findFirst()
                .orElseThrow(UnsupportedFileException::new);
    }
}
