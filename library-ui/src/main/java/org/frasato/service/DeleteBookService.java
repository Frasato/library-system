package org.frasato.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

public class DeleteBookService {

    /**
     * Remove um livro existente via DELETE na API.
     *
     * @throws RuntimeException se a requisição falhar ou retornar status diferente de 200
     */
    public void execute(UUID bookId){
        try{
            HttpClient httpClient = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/v1/book/delete/" + bookId.toString()))
                    .header("Content-Type","application/json")
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() != 200){
                throw new RuntimeException("Error while deleting book: " + response.body());
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
