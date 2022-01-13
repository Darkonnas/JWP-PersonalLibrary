package com.controller;

import com.context.Book;
import com.context.BookReview;
import com.context.BookReviewComment;
import com.context.Friend;
import com.service.BookReviewService;
import com.service.BookService;
import com.service.FriendService;
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

@WebMvcTest(controllers = BookReviewController.class)
@EnableTransactionManagement
class BookReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookReviewService bookReviewService;
    @MockBean
    private BookService bookService;
    @MockBean
    private FriendService friendService;

    private static BookReview bookReviewWithComments;
    private static BookReview bookReviewWithNoComments;
    private static List<BookReview> existingBookReviews;
    private static Book bookWithReviews;
    private static Book bookWithNoReviews;
    private static Friend friendWhoCanReview;
    private static Friend friendWhoCannotReview;

    @BeforeAll
    public static void setup() {
        bookReviewWithComments = new BookReview();
        bookReviewWithNoComments = new BookReview();
        existingBookReviews = List.of(bookReviewWithComments);
        bookWithReviews = new Book();
        bookWithNoReviews = new Book();
        friendWhoCanReview = new Friend();
        friendWhoCannotReview = new Friend();
        BookReviewComment bookReviewComment = new BookReviewComment();

        bookWithReviews.setBookReviews(List.of(bookReviewWithComments));
        bookWithNoReviews.setBookReviews(Collections.emptyList());
        bookReviewWithComments.setComments(List.of(bookReviewComment));
        bookReviewWithNoComments.setComments(Collections.emptyList());
    }

    @Test
    void getBookReviews() throws Exception {
        when(bookReviewService.getBookReviews()).thenReturn(existingBookReviews).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/book-reviews")).andExpect(status().isOk());
        mockMvc.perform(get("/api/book-reviews")).andExpect(status().isNoContent());
    }

    @Test
    void getBookReviewsForBook() throws Exception {
        when(bookService.getBookById(0L)).thenReturn(Optional.of(bookWithReviews));
        when(bookService.getBookById(1L)).thenReturn(Optional.of(bookWithNoReviews));
        when(bookService.getBookById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/book-reviews/book/0")).andExpect(status().isOk());
        mockMvc.perform(get("/api/book-reviews/book/1")).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/book-reviews/book/2")).andExpect(status().isNotFound());
    }

    @Test
    void getBookReview() throws Exception {
        when(bookReviewService.getBookReviewById(0L)).thenReturn(Optional.of(bookReviewWithComments));
        when(bookReviewService.getBookReviewById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/book-reviews/0")).andExpect(status().isOk());
        mockMvc.perform(get("/api/book-reviews/1")).andExpect(status().isNotFound());
    }

    @Test
    void addBookReview() throws Exception {
        when(bookService.getBookById(0L)).thenReturn(Optional.of(bookWithNoReviews));
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());

        when(friendService.getFriendById(0L)).thenReturn(Optional.of(friendWhoCanReview));
        when(friendService.getFriendById(1L)).thenReturn(Optional.of(friendWhoCannotReview));
        when(friendService.getFriendById(2L)).thenReturn(Optional.empty());

        when(bookReviewService.canFriendReviewBook(friendWhoCanReview, bookWithNoReviews)).thenReturn(true);
        when(bookReviewService.canFriendReviewBook(friendWhoCannotReview, bookWithNoReviews)).thenReturn(false);

        mockMvc.perform(post("/api/book-reviews/").contentType(MediaType.APPLICATION_JSON).content("ReviewText")
                        .param("authorScore", "10")
                        .param("bookId", "0")
                        .param("friendId", "0"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/book-reviews/").contentType(MediaType.APPLICATION_JSON).content("ReviewText")
                        .param("authorScore", "10")
                        .param("bookId", "1")
                        .param("friendId", "0"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/book-reviews/").contentType(MediaType.APPLICATION_JSON).content("ReviewText")
                        .param("authorScore", "10")
                        .param("bookId", "0")
                        .param("friendId", "1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/book-reviews/").contentType(MediaType.APPLICATION_JSON).content("ReviewText")
                        .param("authorScore", "10")
                        .param("bookId", "0")
                        .param("friendId", "2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBookReviewText() throws Exception {
        when(bookReviewService.getBookReviewById(0L)).thenReturn(Optional.of(bookReviewWithComments));
        when(bookReviewService.getBookReviewById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/book-reviews/0/text").contentType(MediaType.APPLICATION_JSON).content("REVIEW_TEXT")).andExpect(status().isNoContent());
        mockMvc.perform(put("/api/book-reviews/1/text").contentType(MediaType.APPLICATION_JSON).content("REVIEW_TEXT")).andExpect(status().isNotFound());
    }

    @Test
    void updateBookReviewScore() throws Exception {
        when(bookReviewService.getBookReviewById(0L)).thenReturn(Optional.of(bookReviewWithComments));
        when(bookReviewService.getBookReviewById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/book-reviews/0/score").param("score", "10")).andExpect(status().isNoContent());
        mockMvc.perform(put("/api/book-reviews/1/score").param("score", "10")).andExpect(status().isNotFound());
    }

    @Test
    void updateBookReviewUpvote() throws Exception {
        when(bookReviewService.getBookReviewById(0L)).thenReturn(Optional.of(bookReviewWithComments));
        when(bookReviewService.getBookReviewById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/book-reviews/0/upvote").param("incrementAmount", "10")).andExpect(status().isNoContent());
        mockMvc.perform(post("/api/book-reviews/1/upvote").param("incrementAmount", "10")).andExpect(status().isNotFound());
    }

    @Test
    void updateBookReviewDownvote() throws Exception {
        when(bookReviewService.getBookReviewById(0L)).thenReturn(Optional.of(bookReviewWithComments));
        when(bookReviewService.getBookReviewById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/book-reviews/0/downvote").param("incrementAmount", "10")).andExpect(status().isNoContent());
        mockMvc.perform(post("/api/book-reviews/1/downvote").param("incrementAmount", "10")).andExpect(status().isNotFound());
    }

    @Test
    void deleteBookReview() throws Exception {
        when(bookReviewService.getBookReviewById(0L)).thenReturn(Optional.of(bookReviewWithComments));
        when(bookReviewService.getBookReviewById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/book-reviews/0")).andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/book-reviews/1")).andExpect(status().isNotFound());
    }

    @Test
    void getBookReviewCommentsForBookReview() throws Exception {
        when(bookReviewService.getBookReviewById(0L)).thenReturn(Optional.of(bookReviewWithComments));
        when(bookReviewService.getBookReviewById(1L)).thenReturn(Optional.of(bookReviewWithNoComments));
        when(bookReviewService.getBookReviewById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/book-reviews/0/comments")).andExpect(status().isOk());
        mockMvc.perform(get("/api/book-reviews/1/comments")).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/book-reviews/2/comments")).andExpect(status().isNotFound());
    }

    @Test
    void addBookReviewComment() throws Exception {
        when(bookReviewService.getBookReviewById(0L)).thenReturn(Optional.of(bookReviewWithComments));
        when(bookReviewService.getBookReviewById(1L)).thenReturn(Optional.empty());

        when(friendService.getFriendById(0L)).thenReturn(Optional.of(friendWhoCanReview));
        when(friendService.getFriendById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/book-reviews/0/comments").contentType(MediaType.APPLICATION_JSON).content("ReviewText")
                .param("friendId", "0")).andExpect(status().isCreated());
        mockMvc.perform(post("/api/book-reviews/0/comments").contentType(MediaType.APPLICATION_JSON).content("ReviewText")
                .param("friendId", "1")).andExpect(status().isBadRequest());
        mockMvc.perform(post("/api/book-reviews/1/comments").contentType(MediaType.APPLICATION_JSON).content("ReviewText")
                .param("friendId", "0")).andExpect(status().isNotFound());
    }
}