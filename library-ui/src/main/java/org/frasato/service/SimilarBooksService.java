package org.frasato.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.frasato.dto.UpdateBookDTO;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class SimilarBooksService {
    private final ObjectMapper mapper = new ObjectMapper();

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
            String body = mapper.writeValueAsString(requestUpdate);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/v1/book/" + id))
                    .header("Content-Type","application/json")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200) throw new RuntimeException("Error while updating book: " + response.body());

            return "Atualizado com sucesso!";
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }
}
