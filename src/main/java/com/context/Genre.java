package com.context;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "genre")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", length = 32, nullable = false)
    private String title;

    @Column(name = "description", length = 100)
    private String description;

    @OneToOne
    @JoinColumn(name = "parent_genre_id", referencedColumnName = "id")
    private Genre parentGenre;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "genre")
    @JsonBackReference
    private Set<Book> books;

    public Genre() {
    }

    public Genre(long id, String title, String description, Genre parentGenre, Set<Book> books) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.parentGenre = parentGenre;
        this.books = books;
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

    public Genre getParentGenre() {
        return parentGenre;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void updateGenre(Genre genre) {
        this.title = genre.title;
        this.description = genre.description;
        this.parentGenre = genre.parentGenre;
        this.books = genre.books;
    }

    public enum SortingCriteria {
        TITLE
    }
}
