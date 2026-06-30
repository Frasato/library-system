package org.frasato.service;

import org.frasato.dto.AddBookRequestDTO;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class AddBookService {

    private final WebClient client = WebClient.create("http://localhost:8080");

    /**
     * Cadastra um novo livro a partir do ISBN informado.
     *
     * @param isbn ISBN do livro a ser cadastrado
     * @return mensagem de sucesso ou mensagem retornada pela API
     */
    public String execute(String isbn){
        AddBookRequestDTO requestDTO = new AddBookRequestDTO(isbn);

        try{

            client.post()
                    .uri("/v1/book/create")
                    .bodyValue(requestDTO)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return "Livro criado com sucesso!";
        }catch (WebClientResponseException e) {
            return e.getResponseBodyAsString();
        }catch(Exception e){
            return e.getMessage();
        }
    }
}
