package org.frasato.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookModel {
    private UUID id;
    @JsonProperty("titulo")
    private String title;
    @JsonProperty("dataPublicacao")
    private String publishDate;
    private List<String> isbn;
    @JsonProperty("editora")
    private List<String> publishers;
    @JsonProperty("author")
    private List<AuthorModel> authors;
    @JsonProperty("livrosSemelhantes")
    private List<BookModel> similarBooks;

    public BookModel() {}

    public BookModel(UUID id, String title, String publishDate, List<String> isbn, List<String> publishers, List<AuthorModel> authors, List<BookModel> similarBooks) {
        this.id = id;
        this.title = title;
        this.publishDate = publishDate;
        this.isbn = isbn;
        this.publishers = publishers;
        this.authors = authors;
        this.similarBooks = similarBooks;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public List<String> getIsbn() {
        return isbn;
    }

    public void setIsbn(List<String> isbn) {
        this.isbn = isbn;
    }

    public List<String> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }

    public List<AuthorModel> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorModel> authors) {
        this.authors = authors;
    }

    public List<BookModel> getSimilarBooks() {
        return similarBooks;
    }

    public void setSimilarBooks(List<BookModel> similarBooks) {
        this.similarBooks = similarBooks;
    }
}
