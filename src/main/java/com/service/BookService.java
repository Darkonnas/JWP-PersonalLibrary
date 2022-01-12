package com.service;

import com.context.Book;
import com.context.BookCopy;
import com.context.Shelf;
import com.repository.BookCopyRepository;
import com.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    public void deleteBook(Book book) {
        bookRepository.delete(book);
    }

    public void addBookCopy(BookCopy bookCopy) {
        bookCopyRepository.save(bookCopy);
    }

    public List<BookCopy> getBookCopies() {
        return bookCopyRepository.findAll();
    }

    public Optional<BookCopy> getBookCopyById(Long id) {
        return bookCopyRepository.findById(id);
    }

    public Map<Long, Map<Long, Map<Long, List<Book>>>> getLibraryBookCopiesLayout() {
        Map<Long, Map<Long, Map<Long, List<Book>>>> layout = new HashMap<>();

        List<BookCopy> bookCopies = getBookCopies().stream().filter(bookCopy -> bookCopy.getShelf() != null).collect(Collectors.toList());

        for (BookCopy bookCopy : bookCopies) {
            Shelf shelf = bookCopy.getShelf();
            Long roomNo = shelf.getRoom();
            Long rackNo = shelf.getRack();
            Long level = shelf.getLevel();

            if (!layout.containsKey(roomNo)) {
                layout.put(roomNo, new HashMap<>());
            }

            if (!layout.get(roomNo).containsKey(rackNo)) {
                layout.get(roomNo).put(rackNo, new HashMap<>());
            }

            if (!layout.get(roomNo).get(rackNo).containsKey(level)) {
                layout.get(roomNo).get(rackNo).put(level, new ArrayList<>());
            }

            layout.get(roomNo).get(rackNo).get(level).add(bookCopy.getBook());
        }

        return layout;
    }

    public void saveBookCopy(BookCopy bookCopy) {
        bookCopyRepository.save(bookCopy);
    }
}
