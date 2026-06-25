package com.library.library_backend.services.importers;

import com.library.library_backend.exceptions.ConvertFileException;
import com.library.library_backend.models.Author;
import com.library.library_backend.models.Book;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Importador de livros no formato XML.
 *
 * <p>Espera um arquivo XML com a seguinte estrutura:</p>
 * <pre>{@code
 * <books>
 *   <book>
 *     <titulo>Título do Livro</titulo>
 *     <dataPublicacao>2024-01-01</dataPublicacao>
 *     <isbn>
 *       <item>978-0000000000</item>
 *     </isbn>
 *     <editora>
 *       <item>Nome da Editora</item>
 *     </editora>
 *     <authors>
 *       <author>
 *         <nome>Nome do Autor</nome>
 *       </author>
 *     </authors>
 *   </book>
 * </books>
 * }</pre>
 *
 * @see BookImporter
 * @see ImporterBookFactory
 */
@Component
public class XmlBookImporter implements BookImporter{

    /** {@inheritDoc} */
    @Override
    public boolean supports(String extension) {
        return extension.equals("xml");
    }

    /**
     * {@inheritDoc}
     *
     * @throws com.library.library_backend.exceptions.ConvertFileException
     *         caso ocorra falha no parsing ou conversão do arquivo XML.
     */
    @Override
    public List<Book> importFile(MultipartFile file) {
        List<Book> books = new ArrayList<>();

        try{

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file.getInputStream());

            document.getDocumentElement().normalize();
            NodeList nodes = document.getElementsByTagName("book");

            for(int i = 0; i < nodes.getLength(); i++){
                Element element = (Element) nodes.item(i);

                Book book = new Book();
                book.setTitulo(getValue(element, "titulo"));
                book.setDataPublicacao(getValue(element, "dataPublicacao"));

                List<String> isbn = new ArrayList<>();
                isbn.add(getValue(element, "item"));
                book.setIsbn(isbn);

                List<String> publishers = new ArrayList<>();
                publishers.add(
                        getValue(
                                (Element) element
                                        .getElementsByTagName("editora")
                                        .item(0),
                                "item"
                        )
                );;
                book.setEditora(publishers);

                List<Author> authors = new ArrayList<>();

                NodeList authorNodes = element.getElementsByTagName("author");

                for(int j = 0; j < authorNodes.getLength(); j++){
                    Element authorElement = (Element) authorNodes.item(j);
                    Author author = new Author();
                    author.setNome(getValue(authorElement, "nome"));
                    authors.add(author);
                }

                book.setAuthor(authors);
                books.add(book);
            }

            return books;

        }catch (Exception e) {
            throw new ConvertFileException(e.getMessage());
        }
    }

    /**
     * Extrai o conteúdo textual de uma tag filha dentro de um elemento XML.
     *
     * @param element elemento XML pai onde a busca será realizada.
     * @param tag     nome da tag filha a ser extraída.
     * @return conteúdo textual da tag, sem espaços nas extremidades.
     */
    private String getValue(Element element, String tag){
        return element.getElementsByTagName(tag).item(0).getTextContent().trim();
    }
}
