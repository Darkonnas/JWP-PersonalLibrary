package com.context;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name", length = 64, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 32, nullable = false)
    private String lastName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    @JsonBackReference
    private Set<Book> books;

    public Author() {
    }

    public Author(long id, String firstName, String lastName, Set<Book> books) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.books = books;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void updateAuthor(Author author) {
        this.firstName = author.firstName;
        this.lastName = author.lastName;
        this.books = author.books;
    }

    public enum SortingCriteria {
        FIRST_NAME,
        LAST_NAME
    }
}
