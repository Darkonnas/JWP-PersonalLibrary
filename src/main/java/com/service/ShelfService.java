package com.service;

import com.context.BookCopy;
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

    public Optional<Shelf> getShelfById(Long id) {
        return repository.findById(id);
    }

    public void saveShelf(Shelf shelf) {
        repository.save(shelf);
    }

    public void deleteShelf(Shelf shelf) {
        repository.delete(shelf);
    }

    public boolean canShelfStoreBookCopy(Shelf shelf, BookCopy bookCopy) {
        if (shelf.getCapacity() != null && shelf.getCapacity() == shelf.getBookCopies().size()) {
            return false;
        }

        return shelf.getStartingLetter() == null || shelf.getStartingLetter() == bookCopy.getBook().getTitle().charAt(0);
    }
}
