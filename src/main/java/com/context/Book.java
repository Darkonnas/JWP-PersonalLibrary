package com.context;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", length = 32, nullable = false)
    private String title;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "number_of_pages", nullable = false)
    private long numberOfPages;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    private Author author;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "genre_id", referencedColumnName = "id", nullable = false)
    private Genre genre;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    @JsonBackReference
    private Set<BookCopy> copies;

    public Book() {
    }

    public Book(long id, String title, String description, long numberOfPages, Author author, Genre genre, Shelf shelf, Set<BookCopy> copies, Set<Lend> lends) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.numberOfPages = numberOfPages;
        this.author = author;
        this.genre = genre;
        this.copies = copies;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getNumberOfPages() {
        return numberOfPages;
    }

    public Author getAuthor() {
        return author;
    }

    public Genre getGenre() {
        return genre;
    }

    public Set<BookCopy> getCopies() {
        return copies;
    }

    public void updateBook(Book book) {
        this.title = book.title;
        this.description = book.description;
        this.numberOfPages = book.numberOfPages;
        this.author = book.author;
        this.genre = book.genre;
    }

    public enum SortingCriteria {
        TITLE,
        NUMBER_OF_PAGES
    }
}
