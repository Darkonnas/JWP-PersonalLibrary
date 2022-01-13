package com.controller;

import com.context.BookReviewComment;
import com.service.BookReviewService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookReviewCommentController.class)
@EnableTransactionManagement
class BookReviewCommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookReviewService bookReviewService;

    private static BookReviewComment bookReviewComment;

    @BeforeAll
    public static void setup() {
        bookReviewComment = new BookReviewComment();
    }

    @Test
    void updateBookReviewCommentText() throws Exception {
        when(bookReviewService.getBookReviewCommentById(0L)).thenReturn(Optional.of(bookReviewComment));
        when(bookReviewService.getBookReviewCommentById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/book-review-comments/0/text").contentType(MediaType.APPLICATION_JSON).content("REVIEW_TEXT")).andExpect(status().isNoContent());
        mockMvc.perform(put("/api/book-review-comments/1/text").contentType(MediaType.APPLICATION_JSON).content("COMMENT_TEXT")).andExpect(status().isNotFound());
    }

    @Test
    void updateBookReviewCommentUpvote() throws Exception {
        when(bookReviewService.getBookReviewCommentById(0L)).thenReturn(Optional.of(bookReviewComment));
        when(bookReviewService.getBookReviewCommentById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/book-review-comments/0/upvote").param("incrementAmount", "10")).andExpect(status().isNoContent());
        mockMvc.perform(post("/api/book-review-comments/1/upvote").param("incrementAmount", "10")).andExpect(status().isNotFound());
    }

    @Test
    void updateBookReviewCommentDownvote() throws Exception {
        when(bookReviewService.getBookReviewCommentById(0L)).thenReturn(Optional.of(bookReviewComment));
        when(bookReviewService.getBookReviewCommentById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/book-review-comments/0/downvote").param("incrementAmount", "10")).andExpect(status().isNoContent());
        mockMvc.perform(post("/api/book-review-comments/1/downvote").param("incrementAmount", "10")).andExpect(status().isNotFound());
    }

    @Test
    void deleteBookReviewComment() throws Exception {
        when(bookReviewService.getBookReviewCommentById(0L)).thenReturn(Optional.of(bookReviewComment));
        when(bookReviewService.getBookReviewCommentById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/book-review-comments/0")).andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/book-review-comments/1")).andExpect(status().isNotFound());
    }
}