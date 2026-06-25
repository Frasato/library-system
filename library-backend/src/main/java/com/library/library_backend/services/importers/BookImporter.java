package com.library.library_backend.services.importers;

import com.library.library_backend.models.Book;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * Contrato para importadores de livros a partir de arquivos.
 *
 * <p>Cada implementação é responsável por suportar um formato específico
 * e converter o arquivo em uma lista de {@link Book}.</p>
 */
public interface BookImporter {
    /**
     * Verifica se este importador suporta o formato indicado pela extensão.
     *
     * @param extension extensão do arquivo (ex: {@code "csv"}, {@code "txt"}, {@code "xml"}).
     * @return {@code true} se o formato for suportado, {@code false} caso contrário.
     */
    boolean supports(String extension);

    /**
     * Lê o arquivo e converte seu conteúdo em uma lista de {@link Book}.
     *
     * @param file arquivo multipart a ser processado.
     * @return lista de {@link Book} extraídos do arquivo.
     */
    List<Book> importFile(MultipartFile file);
}
