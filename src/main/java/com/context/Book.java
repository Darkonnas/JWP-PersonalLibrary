package com.context;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 64, nullable = false)
    private String title;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "number_of_pages")
    private Integer numberOfPages;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    private Author author;

    @ManyToOne
    @JoinColumn(name = "genre_id", referencedColumnName = "id", nullable = false)
    private Genre genre;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    @JsonIgnore
    private List<BookCopy> copies;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    @JsonIgnore
    private List<BookReview> bookReviews;

    public Book() {
    }

    public Book(String title, String description, Integer numberOfPages, Author author, Genre genre) {
        this.title = title;
        this.description = description;
        this.numberOfPages = numberOfPages;
        this.author = author;
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public Author getAuthor() {
        return author;
    }

    public Genre getGenre() {
        return genre;
    }

    public List<BookCopy> getCopies() {
        return copies;
    }

    public List<BookReview> getBookReviews() {
        return bookReviews;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCopies(List<BookCopy> copies) {
        this.copies = copies;
    }

    public void setBookReviews(List<BookReview> bookReviews) {
        this.bookReviews = bookReviews;
    }

    public void updateBook(String title, String description, Integer numberOfPages, Author author, Genre genre) {
        this.title = title;
        this.description = description;
        this.numberOfPages = numberOfPages;
        this.author = author;
        this.genre = genre;
    }
}
