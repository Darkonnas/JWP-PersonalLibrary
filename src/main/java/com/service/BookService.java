package com.service;

import com.context.Book;
import com.repository.BookCopyRepository;
import com.repository.BookRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;

    public BookService(BookRepository repository, BookCopyRepository bookCopyRepository) {
        this.bookRepository = repository;
        this.bookCopyRepository = bookCopyRepository;
    }

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getBooksSortedByTitle(Sort.Direction direction) {
        return bookRepository.findAll(Sort.by(direction, "title"));
    }

    public List<Book> getBooksSortedByNumberOfPages(Sort.Direction direction) {
        return bookRepository.findAll(Sort.by(direction, "number_of_pages"));
    }

    public Optional<Book> getBookById(long id) {
        return bookRepository.findById(id);
    }

    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    public void deleteBook(Book book) {
        bookRepository.delete(book);
    }
}
