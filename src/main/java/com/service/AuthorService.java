package com.service;

import com.context.Author;
import com.repository.AuthorRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    private final AuthorRepository repository;

    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    public List<Author> getAuthors() {
        return repository.findAll();
    }

    public List<Author> getAuthorsSortedByFirstName(Sort.Direction direction) {
        return repository.findAll(Sort.by(direction, "first_name"));
    }

    public List<Author> getAuthorsSortedByLastName(Sort.Direction direction) {
        return repository.findAll(Sort.by(direction, "last_name"));
    }

    public Optional<Author> getAuthorById(long id) {
        return repository.findById(id);
    }

    public void saveAuthor(Author author) {
        repository.save(author);
    }

    public void deleteAuthor(Author author) {
        repository.delete(author);
    }
}
