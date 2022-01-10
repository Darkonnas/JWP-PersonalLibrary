package com.service;

import com.context.Author;
import com.repository.AuthorRepository;
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
