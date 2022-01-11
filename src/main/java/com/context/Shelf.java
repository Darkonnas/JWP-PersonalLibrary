package com.context;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "shelf")
public class Shelf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "level", nullable = false)
    private Long level;

    @Column(name = "rack", nullable = false)
    private Long rack;

    @Column(name = "room", nullable = false)
    private Long room;

    @Column(name = "capacity")
    private Long capacity;

    @Column(name = "starting_letter")
    private Character startingLetter;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shelf")
    private List<BookCopy> bookCopies;

    public Shelf() {
    }

    public Long getId() {
        return id;
    }

    public Long getLevel() {
        return level;
    }

    public Long getRack() {
        return rack;
    }

    public Long getRoom() {
        return room;
    }

    public Long getCapacity() {
        return capacity;
    }

    public Character getStartingLetter() {
        return startingLetter;
    }

    public List<BookCopy> getBookCopies() {
        return bookCopies;
    }

    public void updateShelf(Shelf shelf) {
        this.level = shelf.level;
        this.rack = shelf.rack;
        this.room = shelf.room;
        this.startingLetter = shelf.startingLetter;
        this.bookCopies = shelf.bookCopies;
    }
}
