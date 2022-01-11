package com.context;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "book_copy")
public class BookCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "book_condition", nullable = false)
    @Enumerated(EnumType.STRING)
    public BookCondition bookCondition;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "shelf_id", referencedColumnName = "id")
    private Shelf shelf;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bookCopy")
    @JsonIgnore
    private List<Lend> lends;

    public BookCopy() {}

    public BookCopy(BookCondition bookCondition, Book book) {
        this.bookCondition = bookCondition;
        this.book = book;
    }

    public Long getId() {
        return id;
    }

    public BookCondition getBookCondition() {
        return bookCondition;
    }

    public Book getBook() {
        return book;
    }

    public Shelf getShelf() {
        return shelf;
    }
    public List<Lend> getLends() {
        return lends;
    }

    public void setBookCondition(BookCondition bookCondition) {
        this.bookCondition = bookCondition;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    public enum BookCondition {
        GOOD,
        DETERIORATED
    }
}
