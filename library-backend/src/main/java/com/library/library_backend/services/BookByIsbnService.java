package com.library.library_backend.services;

import com.library.library_backend.dto.BookResponseOpenLibraryDto;
import com.library.library_backend.exceptions.BookNotFoundException;
import com.library.library_backend.exceptions.ExternalAPIException;
import com.library.library_backend.exceptions.InvalidISBNException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Service responsável por buscar informações de livros na API da OpenLibrary pelo ISBN.
 *
 * <p>Consome o endpoint público:</p>
 * <pre>{@code GET https://openlibrary.org/isbn/{isbn}.json}</pre>
 *
 * @see <a href="https://openlibrary.org/dev/docs/api">OpenLibrary API Documentation</a>
 */
@Service
public class BookByIsbnService {
    private final RestTemplate restTemplate;

    /**
     * @param restTemplate cliente HTTP para chamadas à API da OpenLibrary.
     */
    public BookByIsbnService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    /**
     * Busca as informações de um livro na OpenLibrary pelo ISBN.
     *
     * @param isbn código ISBN do livro a ser buscado.
     * @return {@link BookResponseOpenLibraryDto} com os dados retornados pela OpenLibrary.
     * @throws InvalidISBNException  caso o ISBN informado seja vazio.
     * @throws BookNotFoundException caso a API retorne resposta nula para o ISBN informado.
     * @throws ExternalAPIException  caso ocorra falha na comunicação com a OpenLibrary.
     */
    public BookResponseOpenLibraryDto fetch(String isbn){
        if(isbn.isEmpty()) throw new InvalidISBNException();

        try{
            String uri = "https://openlibrary.org/isbn/"+ isbn +".json";
            BookResponseOpenLibraryDto response = restTemplate.getForObject(uri, BookResponseOpenLibraryDto.class);

            if(response != null){
                return response;
            }else{
                throw new BookNotFoundException(isbn);
            }

        }catch (RestClientException e){
            throw new ExternalAPIException("Error calling OpenLibrary API: " + e);
        }
    }
}