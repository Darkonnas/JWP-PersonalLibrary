package com.service;

import com.context.Book;
import com.repository.BookRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> getBooks() {
        return repository.findAll();
    }

    public List<Book> getBooksSortedByTitle(Sort.Direction direction) {
        return repository.findAll(Sort.by(direction, "title"));
    }

    public List<Book> getBooksSortedByNumberOfPages(Sort.Direction direction) {
        return repository.findAll(Sort.by(direction, "number_of_pages"));
    }

    public Optional<Book> getBookById(long id) {
        return repository.findById(id);
    }

    public void saveBook(Book book) {
        repository.save(book);
    }

    public void deleteBook(Book book) {
        repository.delete(book);
    }
}
