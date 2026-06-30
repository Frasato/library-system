package org.frasato.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AddBookService {

    /**
     * Cadastra um novo livro a partir do ISBN informado, enviando uma requisição
     * POST para a API. Tanto em caso de erro HTTP quanto de exceção, o método
     * retorna uma mensagem ao invés de lançar exceção — o corpo da resposta de
     * erro (ou a mensagem da exceção capturada) é devolvido diretamente.
     *
     * @param isbn ISBN do livro a ser cadastrado
     * @return mensagem de sucesso caso a API retorne status 201; caso contrário,
     *         o corpo da resposta de erro ou a mensagem da exceção capturada
     */
    public String execute(String isbn){

        String jsonBody = "{\"isbn\":\"" + isbn + "\"}";

        try{
            HttpClient httpClient = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/v1/book/create"))
                    .header("Content-Type","application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() != 201){
                return response.body();
            }

            return "Livro criado com sucesso!";
        }catch(Exception e){
            return e.getMessage();
        }
    }
}
