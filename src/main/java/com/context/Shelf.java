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

    @Column(name = "starting_letter", nullable = false)
    private char startingLetter;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shelf")
    @JsonBackReference
    private Set<Book> books;

    public Shelf() {
    }

    public Shelf(long id, long level, long rack, long room, char startingLetter, Set<Book> books) {
        this.id = id;
        this.level = level;
        this.rack = rack;
        this.room = room;
        this.startingLetter = startingLetter;
        this.books = books;
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

    public char getStartingLetter() {
        return startingLetter;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void updateShelf(Shelf shelf) {
        this.level = shelf.level;
        this.rack = shelf.rack;
        this.room = shelf.room;
        this.startingLetter = shelf.startingLetter;
        this.books = shelf.books;
    }
}
