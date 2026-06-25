package com.library.library_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LibraryBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryBackendApplication.class, args);
	}

}
