package com.context;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", length = 64, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 32, nullable = false)
    private String lastName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    private Set<Book> books;

    public Author() {
    }

    public Long getId() {
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
