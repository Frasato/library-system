package com.library.library_backend.repositories;

import com.library.library_backend.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    @Query("SELECT b FROM Book b JOIN b.livrosSemelhantes s WHERE s.id = :bookId")
    List<Book> findBooksWithSimilar(@Param("bookId") UUID bookId);

    @Query("""
            SELECT b
            FROM Book b
            JOIN b.isbn i
            WHERE i IN :isbn
        """)
    Optional<Book> findByIsbnList(@Param("isbn") List<String> isbn);
}
