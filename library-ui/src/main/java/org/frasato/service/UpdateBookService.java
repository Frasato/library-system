package org.frasato.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.frasato.dto.UpdateBookDTO;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

public class UpdateBookService {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Atualiza um livro existente via PATCH na API.
     * Campos de lista (isbn, publisher, autores) devem ser strings separadas por vírgula.
     *
     * @return mensagem de sucesso em caso de status 200
     * @throws RuntimeException se a requisição falhar ou retornar status diferente de 200
     */
    public String execute(
            String title, String publishDate,
            String isbn, String publisher,
            String autores, String id
    ){
        List<String> publishers = stringToList(publisher);
        List<String> isbns = stringToList(isbn);
        List<String> autoresList = Arrays.stream(autores.split(", ")).toList();

        UpdateBookDTO requestUpdate = new UpdateBookDTO(title, publishDate, isbns, publishers, autoresList, null);

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

    private List<String> stringToList(String item){
        return Arrays
                .stream(item.split(", "))
                .map(String::trim)
                .toList();
    }
}