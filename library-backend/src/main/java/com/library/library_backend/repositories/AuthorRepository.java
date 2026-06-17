package com.library.library_backend.repositories;

import com.library.library_backend.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {}
