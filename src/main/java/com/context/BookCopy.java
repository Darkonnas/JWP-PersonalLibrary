package com.context;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "book_copy")
public class BookCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "book_condition", nullable = false)
    @Enumerated(EnumType.STRING)
    public BookCondition bookCondition;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    private Book book;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "shelf_id", referencedColumnName = "id")
    private Shelf shelf;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bookCopy")
    @JsonBackReference
    private Set<Lend> lends;

    public enum BookCondition {
        GOOD,
        DETERIORATED
    }
}
