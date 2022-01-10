package com.context;

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
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "shelf_id", referencedColumnName = "id")
    private Shelf shelf;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bookCopy")
    private Set<Lend> lends;

    public enum BookCondition {
        GOOD,
        DETERIORATED
    }
}
