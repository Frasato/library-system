package org.frasato.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;

public class ImportBookService {

    private final WebClient client = WebClient.create("http://localhost:8080");

    public void execute(File file){
        MultipartBodyBuilder body = new MultipartBodyBuilder();
        body.part("file", new FileSystemResource(file));

        client.post()
                .uri("/v1/book/import")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body.build()))
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
