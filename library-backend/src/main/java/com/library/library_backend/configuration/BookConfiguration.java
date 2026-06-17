package com.library.library_backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BookConfiguration {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
