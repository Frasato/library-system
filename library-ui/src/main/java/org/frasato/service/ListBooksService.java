package org.frasato.service;

import org.frasato.model.BookModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;

public class ListBooksService {

    private final WebClient client = WebClient.create("http://localhost:8080");

    /**
     * Busca a lista completa de livros cadastrados via GET na API.
     *
     * @throws RuntimeException se a requisição falhar ou a resposta não puder ser desserializada
     */
    public List<BookModel> execute(){
        try{
            return client.get()
                    .uri("/v1/book")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<BookModel>>() {})
                    .block();

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
