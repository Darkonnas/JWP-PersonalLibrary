package com.context;

import javax.persistence.*;
import java.util.List;

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
    private List<Book> books;

    public Genre() {
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

    public List<Book> getBooks() {
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
