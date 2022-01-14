package com.service;

import com.context.*;
import com.repository.AuthorRepository;
import com.repository.BookCopyRepository;
import com.repository.BookRepository;
import com.repository.GenreRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookCopyRepository bookCopyRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private GenreRepository genreRepository;

    private static Book existingBook;
    private static BookCopy existingBookCopy;
    private static Author existingAuthor;
    private static Genre existingGenre;
    private static List<Book> nonEmptyBookResult;
    private static List<Book> emptyBookResult;
    private static List<BookCopy> nonEmptyBookCopyResult;
    private static List<BookCopy> emptyBookCopyResult;
    private static List<Author> nonEmptyAuthorResult;
    private static List<Author> emptyAuthorResult;
    private static List<Genre> nonEmptyGenreResult;
    private static List<Genre> emptyGenreResult;

    @BeforeAll
    public static void setup() {
        existingBook = new Book();
        existingBookCopy = new BookCopy();
        existingAuthor = new Author();
        existingGenre = new Genre();
        nonEmptyBookResult = List.of(existingBook);
        emptyBookResult = Collections.emptyList();
        nonEmptyBookCopyResult = List.of(existingBookCopy);
        emptyBookCopyResult = Collections.emptyList();
        nonEmptyAuthorResult = List.of(existingAuthor);
        emptyAuthorResult = Collections.emptyList();
        nonEmptyGenreResult = List.of(existingGenre);
        emptyGenreResult = Collections.emptyList();
    }

    @Test
    void getBooks() {
        when(bookRepository.findAll()).thenReturn(nonEmptyBookResult).thenReturn(emptyBookResult);

        List<Book> firstResult = bookService.getBooks();
        List<Book> secondResult = bookService.getBooks();

        assertEquals(nonEmptyBookResult, firstResult);
        assertEquals(emptyBookResult, secondResult);
    }

    @Test
    void getBookById() {
        when(bookRepository.findById(0L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Book> firstResult = bookService.getBookById(0L);
        Optional<Book> secondResult = bookService.getBookById(1L);

        assertEquals(Optional.of(existingBook), firstResult);
        assertEquals(Optional.empty(), secondResult);
    }

    @Test
    void getBookCopies() {
        when(bookCopyRepository.findAll()).thenReturn(nonEmptyBookCopyResult).thenReturn(emptyBookCopyResult);

        List<BookCopy> firstResult = bookService.getBookCopies();
        List<BookCopy> secondResult = bookService.getBookCopies();

        assertEquals(nonEmptyBookCopyResult, firstResult);
        assertEquals(emptyBookCopyResult, secondResult);
    }

    @Test
    void getBookCopyById() {
        when(bookCopyRepository.findById(0L)).thenReturn(Optional.of(existingBookCopy));
        when(bookCopyRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<BookCopy> firstResult = bookService.getBookCopyById(0L);
        Optional<BookCopy> secondResult = bookService.getBookCopyById(1L);

        assertEquals(Optional.of(existingBookCopy), firstResult);
        assertEquals(Optional.empty(), secondResult);
    }

    @Test
    void getLibraryBookCopiesLayout() {
    }

    @Test
    void getAuthors() {
        when(authorRepository.findAll()).thenReturn(nonEmptyAuthorResult).thenReturn(emptyAuthorResult);

        List<Author> firstResult = bookService.getAuthors();
        List<Author> secondResult = bookService.getAuthors();

        assertEquals(nonEmptyAuthorResult, firstResult);
        assertEquals(emptyAuthorResult, secondResult);
    }

    @Test
    void getAuthorById() {
        when(authorRepository.findById(0L)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Author> firstResult = bookService.getAuthorById(0L);
        Optional<Author> secondResult = bookService.getAuthorById(1L);

        assertEquals(Optional.of(existingAuthor), firstResult);
        assertEquals(Optional.empty(), secondResult);
    }

    @Test
    void getGenres() {
        when(genreRepository.findAll()).thenReturn(nonEmptyGenreResult).thenReturn(emptyGenreResult);

        List<Genre> firstResult = bookService.getGenres();
        List<Genre> secondResult = bookService.getGenres();

        assertEquals(nonEmptyGenreResult, firstResult);
        assertEquals(emptyGenreResult, secondResult);
    }

    @Test
    void getGenreById() {
        when(genreRepository.findById(0L)).thenReturn(Optional.of(existingGenre));
        when(genreRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Genre> firstResult = bookService.getGenreById(0L);
        Optional<Genre> secondResult = bookService.getGenreById(1L);

        assertEquals(Optional.of(existingGenre), firstResult);
        assertEquals(Optional.empty(), secondResult);
    }
}