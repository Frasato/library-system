package org.frasato.service;

import org.springframework.web.reactive.function.client.WebClient;
import java.util.UUID;

public class DeleteBookService {

    private final WebClient client = WebClient.create("http://localhost:8080");

    /**
     * Remove um livro existente via DELETE na API.
     *
     * @throws RuntimeException se a requisição falhar ou retornar status diferente de 200
     */
    public void execute(UUID bookId){
        try{
            client.delete()
                    .uri("/v1/book/delete/{id}", bookId.toString())
                    .retrieve()
                    .toBodilessEntity()
                    .block();

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
