package com.service;

import com.context.*;
import com.repository.BookReviewCommentRepository;
import com.repository.BookReviewRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookReviewServiceTest {
    @InjectMocks
    private BookReviewService bookReviewService;

    @Mock
    private BookReviewRepository bookReviewRepository;
    @Mock
    private BookReviewCommentRepository bookReviewCommentRepository;

    private static BookReview existingBookReview;
    private static List<BookReview> nonEmptyResult;
    private static List<BookReview> emptyResult;
    private static BookReviewComment existingBookReviewComment;
    private static Book book;
    private static Friend friendWithPreviousLend;
    private static Friend friendWithoutPreviousLend;

    @BeforeAll
    public static void setup()  {
        existingBookReview = new BookReview();
        nonEmptyResult = List.of(existingBookReview);
        emptyResult = Collections.emptyList();
        book = new Book();
        BookCopy bookCopy = new BookCopy();
        bookCopy.setBook(book);
        existingBookReviewComment = new BookReviewComment();
        existingBookReview.setComments(List.of(existingBookReviewComment));
        Lend lend = new Lend();
        lend.setBookCopy(bookCopy);
        friendWithPreviousLend = new Friend();
        friendWithPreviousLend.setLends(List.of(lend));
        friendWithoutPreviousLend = new Friend();
        friendWithoutPreviousLend.setLends(Collections.emptyList());
    }

    @Test
    void getBookReviews() {
        when(bookReviewRepository.findAll()).thenReturn(nonEmptyResult).thenReturn(emptyResult);

        List<BookReview> firstResult = bookReviewService.getBookReviews();
        List<BookReview> secondResult = bookReviewService.getBookReviews();

        assertEquals(nonEmptyResult, firstResult);
        assertEquals(emptyResult, secondResult);
    }

    @Test
    void getBookReviewById() {
        when(bookReviewRepository.findById(0L)).thenReturn(Optional.of(existingBookReview));
        when(bookReviewRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<BookReview> firstResult = bookReviewService.getBookReviewById(0L);
        Optional<BookReview> secondResult = bookReviewService.getBookReviewById(1L);

        assertEquals(Optional.of(existingBookReview), firstResult);
        assertEquals(Optional.empty(), secondResult);
    }

    @Test
    void getBookReviewCommentById() {
        when(bookReviewCommentRepository.findById(0L)).thenReturn(Optional.of(existingBookReviewComment));
        when(bookReviewCommentRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<BookReviewComment> firstResult = bookReviewService.getBookReviewCommentById(0L);
        Optional<BookReviewComment> secondResult = bookReviewService.getBookReviewCommentById(1L);

        assertEquals(Optional.of(existingBookReviewComment), firstResult);
        assertEquals(Optional.empty(), secondResult);
    }

    @Test
    void incrementBookReviewUpvoteCount() {
        existingBookReview.setUpvoteCount(0);
        bookReviewService.incrementBookReviewUpvoteCount(existingBookReview, 10);
        assertEquals(10, existingBookReview.getUpvoteCount());
    }

    @Test
    void incrementBookReviewDownvoteCount() {
        existingBookReview.setDownvoteCount(0);
        bookReviewService.incrementBookReviewDownvoteCount(existingBookReview, 10);
        assertEquals(10, existingBookReview.getDownvoteCount());
    }

    @Test
    void incrementBookReviewCommentUpvoteCount() {
        existingBookReviewComment.setUpvoteCount(0);
        bookReviewService.incrementBookReviewCommentUpvoteCount(existingBookReviewComment, 10);
        assertEquals(10, existingBookReviewComment.getUpvoteCount());
    }

    @Test
    void incrementBookReviewCommentDownvoteCount() {
        existingBookReviewComment.setDownvoteCount(0);
        bookReviewService.incrementBookReviewCommentDownvoteCount(existingBookReviewComment, 10);
        assertEquals(10, existingBookReviewComment.getDownvoteCount());
    }

    @Test
    void canFriendReviewBook() {
        assertTrue(bookReviewService.canFriendReviewBook(friendWithPreviousLend, book));
        assertFalse(bookReviewService.canFriendReviewBook(friendWithoutPreviousLend, book));
    }
}