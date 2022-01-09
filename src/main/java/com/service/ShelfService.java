package com.service;

import com.context.Shelf;
import com.repository.ShelfRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShelfService {
    private final ShelfRepository repository;

    public ShelfService(ShelfRepository repository) {
        this.repository = repository;
    }

    public List<Shelf> getShelves() {
        return repository.findAll();
    }

    public List<Shelf> getShelvesByLevel(long level) {
        return repository.findAllByLevel(level);
    }

    public List<Shelf> getShelvesByRack(long rack) {
        return repository.findAllByRack(rack);
    }

    public List<Shelf> getShelvesByRoom(long room) {
        return repository.findAllByRoom(room);
    }

    public List<Shelf> getShelvesByStartingLetter(char startingLetter) {
        return repository.findAllByStartingLetter(startingLetter);
    }

    public Optional<Shelf> getShelfById(long id) {
        return repository.findById(id);
    }

    public void saveShelf(Shelf shelf) {
        repository.save(shelf);
    }

    public void deleteShelf(Shelf shelf) {
        repository.delete(shelf);
    }
}
