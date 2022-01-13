package com.service;

import com.context.*;
import com.repository.AuthorRepository;
import com.repository.BookCopyRepository;
import com.repository.BookRepository;
import com.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookService(BookRepository repository, BookCopyRepository bookCopyRepository, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.bookRepository = repository;
        this.bookCopyRepository = bookCopyRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
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

    public Map<Integer, Map<Integer, Map<Integer, List<Book>>>> getLibraryBookCopiesLayout() {
        Map<Integer, Map<Integer, Map<Integer, List<Book>>>> layout = new HashMap<>();

        List<BookCopy> bookCopies = getBookCopies().stream().filter(bookCopy -> bookCopy.getShelf() != null).collect(Collectors.toList());

        for (BookCopy bookCopy : bookCopies) {
            Shelf shelf = bookCopy.getShelf();
            Integer roomNo = shelf.getRoom();
            Integer rackNo = shelf.getRack();
            Integer level = shelf.getLevel();

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

    public List<Author> getAuthors() {
        return authorRepository.findAll();
    }

    public Optional<Author> getAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    public void saveAuthor(Author author) {
        authorRepository.save(author);
    }

    public void deleteAuthor(Author author) {
        authorRepository.delete(author);
    }

    public List<Genre> getGenres() {
        return genreRepository.findAll();
    }

    public Optional<Genre> getGenreById(Long id) {
        return genreRepository.findById(id);
    }

    public void saveGenre(Genre genre) {
        genreRepository.save(genre);
    }

    public void deleteGenre(Genre genre) {
        genreRepository.delete(genre);
    }
}
