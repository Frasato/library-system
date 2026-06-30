package org.frasato.service;

import org.frasato.dto.UpdateBookDTO;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Arrays;
import java.util.List;

public class UpdateBookService {

    private final WebClient client = WebClient.create("http://localhost:8080");

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

    private List<String> stringToList(String item){
        return Arrays
                .stream(item.split(", "))
                .map(String::trim)
                .toList();
    }
}