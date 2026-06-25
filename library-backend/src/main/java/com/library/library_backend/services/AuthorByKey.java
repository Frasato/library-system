package com.library.library_backend.services;

import com.library.library_backend.dto.AuthorResponseOpenLibraryDto;
import com.library.library_backend.exceptions.AuthorNotFoundException;
import com.library.library_backend.exceptions.ExternalAPIException;
import com.library.library_backend.exceptions.InvalidKeyException;
import com.library.library_backend.mappers.AuthorMapper;
import com.library.library_backend.models.Author;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Service responsável por buscar informações de autores na API da OpenLibrary pela Key.
 *
 * <p>Consome o endpoint público:</p>
 * <pre>{@code GET https://openlibrary.org/{key}.json}</pre>
 *
 * <p>Exemplo de Key: {@code /authors/OL23919A}</p>
 *
 * @see <a href="https://openlibrary.org/dev/docs/api">OpenLibrary API Documentation</a>
 */
@Service
public class AuthorByKey {
    private final RestTemplate restTemplate;
    private final AuthorMapper authorMapper;

    /**
     * @param restTemplate cliente HTTP para chamadas à API da OpenLibrary.
     * @param authorMapper mapper para conversão de {@link AuthorResponseOpenLibraryDto} para {@link Author}.
     */
    public AuthorByKey(RestTemplate restTemplate, AuthorMapper authorMapper){
        this.restTemplate = restTemplate;
        this.authorMapper = authorMapper;
    }

    /**
     * Busca as informações de um autor na OpenLibrary pela Key e converte para {@link Author}.
     *
     * <p>Após a conversão, a Key original é aplicada na entidade via {@code author.setKey(key)},
     * pois esse campo não está presente no corpo da resposta da API.</p>
     *
     * @param key identificador único do autor na OpenLibrary (ex: {@code /authors/OL23919A}).
     * @return {@link Author} com os dados retornados pela OpenLibrary.
     * @throws InvalidKeyException   caso a Key informada seja vazia.
     * @throws AuthorNotFoundException caso a API retorne resposta nula para a Key informada.
     * @throws ExternalAPIException  caso ocorra falha na comunicação com a OpenLibrary.
     */
    public Author fetch(String key){
        if(key.isEmpty()) throw new InvalidKeyException();

        try{
            String uri = "https://openlibrary.org"+ key +".json";
            AuthorResponseOpenLibraryDto response = restTemplate.getForObject(uri, AuthorResponseOpenLibraryDto.class);

            if(response != null){
                Author author = authorMapper.toEntity(response);
                author.setKey(key);
                return author;
            }else{
                throw new AuthorNotFoundException(key);
            }

        }catch(RestClientException e){
            throw new ExternalAPIException("Error calling OpenLibrary API: " + e);
        }
    }
}
