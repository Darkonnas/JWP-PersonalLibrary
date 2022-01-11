package com.context;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "genre")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 32, nullable = false)
    private String title;

    @Column(name = "description", length = 500)
    private String description;

    @OneToOne
    @JoinColumn(name = "parent_genre_id", referencedColumnName = "id")
    private Genre parentGenre;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "genre")
    private Set<Book> books;

    public Genre() {
    }

    public Genre(String title, String description, Genre parentGenre, Set<Book> books) {
        this.title = title;
        this.description = description;
        this.parentGenre = parentGenre;
        this.books = books;
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
