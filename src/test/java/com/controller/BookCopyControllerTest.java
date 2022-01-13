package com.controller;

import com.context.BookCopy;
import com.context.Lend;
import com.context.Shelf;
import com.service.BookService;
import com.service.LendService;
import com.service.ShelfService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookCopyController.class)
@EnableTransactionManagement
class BookCopyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;
    @MockBean
    private ShelfService shelfService;
    @MockBean
    private LendService lendService;

    private static BookCopy bookCopyOnAShelf;
    private static BookCopy bookCopyCurrentlyLent;
    private static Shelf shelf;
    private static Shelf unavailableShelf;

    @BeforeAll
    public static void setup() {
        bookCopyOnAShelf = new BookCopy();
        bookCopyCurrentlyLent = new BookCopy();
        shelf = new Shelf();
        unavailableShelf = new Shelf();
        Lend lend = new Lend();

        bookCopyOnAShelf.setShelf(shelf);
    }

    @Test
    void updateBookCopyBookCondition() throws Exception {
        when(bookService.getBookCopyById(0L)).thenReturn(Optional.of(bookCopyOnAShelf));
        when(bookService.getBookCopyById(1L)).thenReturn(Optional.of(bookCopyCurrentlyLent));
        when(bookService.getBookCopyById(2L)).thenReturn(Optional.empty());

        when(lendService.isBookCurrentlyLend(bookCopyCurrentlyLent)).thenReturn(true);

        mockMvc.perform(put("/api/book-copies/0/condition").param("bookCondition", "DETERIORATED")).andExpect(status().isNoContent());
        mockMvc.perform(put("/api/book-copies/1/condition").param("bookCondition", "DETERIORATED")).andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/book-copies/2/condition").param("bookCondition", "DETERIORATED")).andExpect(status().isNotFound());
    }

    @Test
    void updateBookCopyShelf() throws Exception {
        when(bookService.getBookCopyById(0L)).thenReturn(Optional.of(bookCopyOnAShelf));
        when(bookService.getBookCopyById(1L)).thenReturn(Optional.of(bookCopyCurrentlyLent));
        when(bookService.getBookCopyById(2L)).thenReturn(Optional.empty());

        when(lendService.isBookCurrentlyLend(bookCopyCurrentlyLent)).thenReturn(true);

        when(shelfService.getShelfById(0L)).thenReturn(Optional.of(shelf));
        when(shelfService.getShelfById(1L)).thenReturn(Optional.of(unavailableShelf));
        when(shelfService.getShelfById(2L)).thenReturn(Optional.empty());

        when(shelfService.canShelfStoreBookCopy(shelf, bookCopyOnAShelf)).thenReturn(true);
        when(shelfService.canShelfStoreBookCopy(unavailableShelf, bookCopyOnAShelf)).thenReturn(false);

        mockMvc.perform(put("/api/book-copies/0/shelf").param("shelfId", "0")).andExpect(status().isNoContent());
        mockMvc.perform(put("/api/book-copies/1/shelf").param("shelfId", "0")).andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/book-copies/2/shelf").param("shelfId", "0")).andExpect(status().isNotFound());
        mockMvc.perform(put("/api/book-copies/0/shelf").param("shelfId", "1")).andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/book-copies/0/shelf").param("shelfId", "2")).andExpect(status().isBadRequest());
    }
}