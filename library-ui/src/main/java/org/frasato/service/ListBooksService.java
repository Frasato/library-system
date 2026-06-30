package org.frasato.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.frasato.model.BookModel;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


public class ListBooksService {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Busca a lista completa de livros cadastrados via GET na API.
     *
     * @throws RuntimeException se a requisição falhar ou a resposta não puder ser desserializada
     */
    public List<BookModel> execute(){
        try{
            HttpClient httpClient = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/v1/book"))
                    .header("Content-Type","application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return mapper.readValue(
                    response.body(),
                    new TypeReference<List<BookModel>>(){}
            );
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
