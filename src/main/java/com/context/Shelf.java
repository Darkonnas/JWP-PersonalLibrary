package com.context;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "shelf")
public class Shelf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "rack", nullable = false)
    private Integer rack;

    @Column(name = "room", nullable = false)
    private Integer room;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "starting_letter")
    private Character startingLetter;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shelf")
    @JsonIgnore
    private List<BookCopy> bookCopies;

    public Shelf() {
    }

    public Long getId() {
        return id;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getRack() {
        return rack;
    }

    public Integer getRoom() {
        return room;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Character getStartingLetter() {
        return startingLetter;
    }

    public List<BookCopy> getBookCopies() {
        return bookCopies;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setStartingLetter(Character startingLetter) {
        this.startingLetter = startingLetter;
    }

    public void setBookCopies(List<BookCopy> bookCopies) {
        this.bookCopies = bookCopies;
    }

    public void updateShelf(Shelf shelf) {
        this.level = shelf.level;
        this.rack = shelf.rack;
        this.room = shelf.room;
        this.startingLetter = shelf.startingLetter;
        this.bookCopies = shelf.bookCopies;
    }
}
