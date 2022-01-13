package com.controller;

import com.context.Author;
import com.context.Book;
import com.context.BookCopy;
import com.context.Genre;
import com.service.BookService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class)
@EnableTransactionManagement
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private static Author author;
    private static Genre genre;
    private static Book bookWithCopies;
    private static Book bookWithoutCopies;
    private static List<Book> existingBooks;

    @BeforeAll
    public static void setup() {
        author = new Author();
        genre = new Genre();
        bookWithCopies = new Book("Book1_Title", "Book1_Description", 100, author, genre);
        bookWithoutCopies = new Book("Book2_Title", "Book2_Description", 100, author, genre);
        existingBooks = List.of(bookWithCopies, bookWithoutCopies);
        BookCopy bookCopy = new BookCopy(BookCopy.BookCondition.GOOD, bookWithCopies);
        bookWithCopies.setCopies(List.of(bookCopy));
        bookWithoutCopies.setCopies(Collections.emptyList());
    }

    @Test
    void getAllBooks() throws Exception {
        when(bookService.getBooks()).thenReturn(existingBooks).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/books")).andExpect(status().isOk());
        mockMvc.perform(get("/api/books")).andExpect(status().isNoContent());
    }

    @Test
    void getBook() throws Exception {
        when(bookService.getBookById(0L)).thenReturn(Optional.of(bookWithCopies));
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/0")).andExpect(status().isOk());
        mockMvc.perform(get("/api/books/1")).andExpect(status().isNotFound());
    }

    @Test
    void getBookCopies() throws Exception {
        when(bookService.getBookById(0L)).thenReturn(Optional.of(bookWithCopies));
        when(bookService.getBookById(1L)).thenReturn(Optional.of(bookWithoutCopies));
        when(bookService.getBookById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/0/copies")).andExpect(status().isOk());
        mockMvc.perform(get("/api/books/1/copies")).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/books/2/copies")).andExpect(status().isNotFound());
    }

    @Test
    void addBook() throws Exception {
        when(bookService.getAuthorById(0L)).thenReturn(Optional.of(author));
        when(bookService.getAuthorById(1L)).thenReturn(Optional.empty());

        when(bookService.getGenreById(0L)).thenReturn(Optional.of(genre));
        when(bookService.getGenreById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/books/").contentType(MediaType.APPLICATION_JSON).content("BookDescription")
                        .param("title", "BookTitle")
                        .param("numberOfPages", "100")
                        .param("authorId", "0")
                        .param("genreId", "0"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/books/").contentType(MediaType.APPLICATION_JSON).content("BookDescription")
                        .param("title", "BookTitle")
                        .param("numberOfPages", "100")
                        .param("authorId", "1")
                        .param("genreId", "0"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/books/").contentType(MediaType.APPLICATION_JSON).content("BookDescription")
                        .param("title", "BookTitle")
                        .param("numberOfPages", "100")
                        .param("authorId", "0")
                        .param("genreId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addBookCopy() throws Exception {
        when(bookService.getBookById(0L)).thenReturn(Optional.of(bookWithoutCopies));
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/books/0/copies").contentType(MediaType.APPLICATION_JSON).param("bookCondition", "GOOD")).andExpect(status().isCreated());
        mockMvc.perform(post("/api/books/1/copies").contentType(MediaType.APPLICATION_JSON).param("bookCondition", "GOOD")).andExpect(status().isNotFound());
    }

    @Test
    void updateBook() throws Exception {
        when(bookService.getBookById(0L)).thenReturn(Optional.of(bookWithCopies));
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());

        when(bookService.getAuthorById(0L)).thenReturn(Optional.of(author));
        when(bookService.getAuthorById(1L)).thenReturn(Optional.empty());

        when(bookService.getGenreById(0L)).thenReturn(Optional.of(genre));
        when(bookService.getGenreById(1L)).thenReturn(Optional.empty());


        mockMvc.perform(put("/api/books/0").contentType(MediaType.APPLICATION_JSON).content("BookDescription")
                        .param("title", "BookTitle")
                        .param("numberOfPages", "100")
                        .param("authorId", "0")
                        .param("genreId", "0"))
                .andExpect(status().isNoContent());

        mockMvc.perform(put("/api/books/1").contentType(MediaType.APPLICATION_JSON).content("BookDescription")
                        .param("title", "BookTitle")
                        .param("numberOfPages", "100")
                        .param("authorId", "0")
                        .param("genreId", "0"))
                .andExpect(status().isNotFound());

        mockMvc.perform(put("/api/books/0").contentType(MediaType.APPLICATION_JSON).content("BookDescription")
                        .param("title", "BookTitle")
                        .param("numberOfPages", "100")
                        .param("authorId", "1")
                        .param("genreId", "0"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(put("/api/books/0").contentType(MediaType.APPLICATION_JSON).content("BookDescription")
                        .param("title", "BookTitle")
                        .param("numberOfPages", "100")
                        .param("authorId", "0")
                        .param("genreId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteBook() throws Exception {
        when(bookService.getBookById(0L)).thenReturn(Optional.of(bookWithCopies));
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/books/0")).andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/books/1")).andExpect(status().isNotFound());
    }
}
