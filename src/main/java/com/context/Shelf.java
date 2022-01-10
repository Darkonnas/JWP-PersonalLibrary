package com.context;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "shelf")
public class Shelf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "level", nullable = false)
    private long level;

    @Column(name = "rack", nullable = false)
    private long rack;

    @Column(name = "room", nullable = false)
    private long room;

    @Column(name = "capacity")
    private long capacity;

    @Column(name = "starting_letter")
    private char startingLetter;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shelf")
    @JsonBackReference
    private Set<BookCopy> bookCopies;

    public Shelf() {
    }

    public Shelf(long id, long level, long rack, long room, long capacity, char startingLetter, Set<BookCopy> bookCopies) {
        this.id = id;
        this.level = level;
        this.rack = rack;
        this.room = room;
        this.capacity = capacity;
        this.startingLetter = startingLetter;
        this.bookCopies = bookCopies;
    }

    public long getId() {
        return id;
    }

    public long getLevel() {
        return level;
    }

    public long getRack() {
        return rack;
    }

    public long getRoom() {
        return room;
    }

    public long getCapacity() {
        return capacity;
    }

    public char getStartingLetter() {
        return startingLetter;
    }

    public Set<BookCopy> getBookCopies() {
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
