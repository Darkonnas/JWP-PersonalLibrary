package com.controller;

import com.context.*;
import com.service.BookService;
import com.service.FriendService;
import com.service.LendService;
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

@WebMvcTest(controllers = LendController.class)
@EnableTransactionManagement
class LendControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LendService lendService;
    @MockBean
    private BookService bookService;
    @MockBean
    private FriendService friendService;

    private static Lend lendWithExtensions;
    private static Lend lendWithoutExtensions;
    private static Lend lendThatCanBeExtended;
    private static Lend lendThatCannotBeExtended;
    private static List<Lend> existingLends;
    private static BookCopy bookCopyWithLends;
    private static BookCopy bookCopyWithNoLends;
    private static Book bookThatCanBeLent;
    private static Book bookThatCannotBeLent;
    private static Friend friendThatCanBorrowBooks;
    private static Friend friendThatCannotBorrowBooks;

    @BeforeAll
    public static void setup() {
        lendWithExtensions = new Lend();
        lendWithoutExtensions = new Lend();
        lendThatCanBeExtended = new Lend();
        lendThatCannotBeExtended = new Lend();
        existingLends = List.of(lendWithExtensions, lendWithoutExtensions);
        bookCopyWithLends = new BookCopy();
        bookCopyWithNoLends = new BookCopy();
        bookThatCanBeLent = new Book();
        bookThatCannotBeLent = new Book();
        friendThatCanBorrowBooks = new Friend();
        friendThatCannotBorrowBooks = new Friend();
        LendExtension lendExtension = new LendExtension();

        lendWithExtensions.setLendExtensions(List.of(lendExtension));
        lendWithoutExtensions.setLendExtensions(Collections.emptyList());
    }

    @Test
    void getAllLends() throws Exception {
        when(lendService.getLends()).thenReturn(existingLends).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/lends")).andExpect(status().isOk());
        mockMvc.perform(get("/api/lends")).andExpect(status().isNoContent());
    }

    @Test
    void getLend() throws Exception {
        when(lendService.getLendById(0L)).thenReturn(Optional.of(lendWithExtensions));
        when(lendService.getLendById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/lends/0")).andExpect(status().isOk());
        mockMvc.perform(get("/api/lends/1")).andExpect(status().isNotFound());
    }

    @Test
    void getLastBorrowerOfBookCopy() throws Exception {
        when(bookService.getBookCopyById(0L)).thenReturn(Optional.of(bookCopyWithLends));
        when(bookService.getBookCopyById(1L)).thenReturn(Optional.of(bookCopyWithNoLends));
        when(bookService.getBookCopyById(2L)).thenReturn(Optional.empty());

        when(lendService.getLastLendOfBookCopy(bookCopyWithLends)).thenReturn(Optional.of(lendWithExtensions));
        when(lendService.getLastLendOfBookCopy(bookCopyWithNoLends)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/lends/last-borrower/0")).andExpect(status().isOk());
        mockMvc.perform(get("/api/lends/last-borrower/1")).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/lends/last-borrower/2")).andExpect(status().isBadRequest());
    }

    @Test
    void addLend() throws Exception {
        when(bookService.getBookById(0L)).thenReturn(Optional.of(bookThatCanBeLent));
        when(bookService.getBookById(1L)).thenReturn(Optional.of(bookThatCannotBeLent));
        when(bookService.getBookById(2L)).thenReturn(Optional.empty());

        when(friendService.getFriendById(0L)).thenReturn(Optional.of(friendThatCanBorrowBooks));
        when(friendService.getFriendById(1L)).thenReturn(Optional.of(friendThatCannotBorrowBooks));
        when(bookService.getBookCopyById(2L)).thenReturn(Optional.empty());

        when(lendService.getAvailableCopyForLending(bookThatCanBeLent)).thenReturn(Optional.of(bookCopyWithNoLends));
        when(lendService.getAvailableCopyForLending(bookThatCannotBeLent)).thenReturn(Optional.empty());

        when(lendService.canFriendBorrowBooks(friendThatCanBorrowBooks)).thenReturn(true);
        when(lendService.canFriendBorrowBooks(friendThatCannotBorrowBooks)).thenReturn(false);

        mockMvc.perform(post("/api/lends/")
                        .param("bookId", "0")
                        .param("friendId", "0")
                        .param("lendDuration", "30"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/lends/")
                        .param("bookId", "1")
                        .param("friendId", "0")
                        .param("lendDuration", "30"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/lends/")
                        .param("bookId", "0")
                        .param("friendId", "1")
                        .param("lendDuration", "30"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/lends/")
                        .param("bookId", "0")
                        .param("friendId", "2")
                        .param("lendDuration", "30"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/lends/")
                        .param("bookId", "2")
                        .param("friendId", "0")
                        .param("lendDuration", "30"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateLendStatus() throws Exception {
        when(lendService.getLendById(0L)).thenReturn(Optional.of(lendWithExtensions));
        when(lendService.getLendById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/lends/0/status").param("status", "RETURNED")).andExpect(status().isNoContent());
        mockMvc.perform(put("/api/lends/1/status").param("status", "RETURNED")).andExpect(status().isNotFound());
    }

    @Test
    void getLendExtensionsForLend() throws Exception {
        when(lendService.getLendById(0L)).thenReturn(Optional.of(lendWithExtensions));
        when(lendService.getLendById(1L)).thenReturn(Optional.of(lendWithoutExtensions));
        when(lendService.getLendById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/lends/0/extensions")).andExpect(status().isOk());
        mockMvc.perform(get("/api/lends/1/extensions")).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/lends/2/extensions")).andExpect(status().isNotFound());
    }

    @Test
    void requestLendExtension() throws Exception {
        when(lendService.getLendById(0L)).thenReturn(Optional.of(lendThatCanBeExtended));
        when(lendService.getLendById(1L)).thenReturn(Optional.of(lendThatCannotBeExtended));
        when(lendService.getLendById(2L)).thenReturn(Optional.empty());

        when(lendService.canExtensionBeRequestedForLend(lendThatCanBeExtended)).thenReturn(true);
        when(lendService.canExtensionBeRequestedForLend(lendThatCannotBeExtended)).thenReturn(false);

        mockMvc.perform(post("/api/lends/0/extensions").contentType(MediaType.APPLICATION_JSON).content("ExtensionReason")
                .param("extensionDuration", "10"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/lends/1/extensions").contentType(MediaType.APPLICATION_JSON).content("ExtensionReason")
                        .param("extensionDuration", "10"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/lends/2/extensions").contentType(MediaType.APPLICATION_JSON).content("ExtensionReason")
                        .param("extensionDuration", "10"))
                .andExpect(status().isNotFound());
    }
}