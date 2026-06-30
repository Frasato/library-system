package org.frasato.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.frasato.dto.UpdateBookDTO;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class SimilarBooksService {
    private final WebClient client = WebClient.create("http://localhost:8080");

    /**
     * Associa uma lista de livros semelhantes a um livro específico via PATCH na API.
     *
     * @param booksId IDs dos livros marcados como semelhantes
     * @param id ID do livro que receberá as associações
     * @return mensagem de validação se a lista estiver vazia, ou de sucesso em caso de status 200
     * @throws RuntimeException se a requisição falhar ou retornar status diferente de 200
     */
    public String execute(List<String> booksId, String id){
        if(booksId.isEmpty()) {
            return "Selecione algum livro!";
        }

        UpdateBookDTO requestUpdate = new UpdateBookDTO(null, null, null, null, null, booksId);

        try{
            client.patch()
                    .uri("/v1/book/{id}", id)
                    .bodyValue(requestUpdate)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return "Atualizado com sucesso!";
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
