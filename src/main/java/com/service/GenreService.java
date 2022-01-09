package com.service;

import com.context.Genre;
import com.repository.GenreRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    private final GenreRepository repository;

    public GenreService(GenreRepository repository) {
        this.repository = repository;
    }

    public List<Genre> getGenres() {
        return repository.findAll();
    }

    public List<Genre> getGenresSortedByTitle(Sort.Direction direction) {
        return repository.findAll(Sort.by(direction, "title"));
    }

    public Optional<Genre> getGenreById(long id) {
        return repository.findById(id);
    }

    public void saveGenre(Genre genre) {
        repository.save(genre);
    }

    public void deleteGenre(Genre genre) {
        repository.delete(genre);
    }
}
