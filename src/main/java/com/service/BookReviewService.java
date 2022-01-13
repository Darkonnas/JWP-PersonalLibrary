package com.service;

import com.context.Book;
import com.context.BookReview;
import com.context.BookReviewComment;
import com.context.Friend;
import com.repository.BookReviewCommentRepository;
import com.repository.BookReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookReviewService {
    private final BookReviewRepository bookReviewRepository;
    private final BookReviewCommentRepository bookReviewCommentRepository;

    public BookReviewService(BookReviewRepository bookReviewRepository, BookReviewCommentRepository bookReviewCommentRepository) {
        this.bookReviewRepository = bookReviewRepository;
        this.bookReviewCommentRepository = bookReviewCommentRepository;
    }

    public List<BookReview> getBookReviews() {
        return bookReviewRepository.findAll();
    }

    public Optional<BookReview> getBookReviewById(Long id) {
        return bookReviewRepository.findById(id);
    }

    public Optional<BookReviewComment> getBookReviewCommentById(Long id) {
        return bookReviewCommentRepository.findById(id);
    }

    public void saveBookReview(BookReview bookReview) {
        bookReviewRepository.save(bookReview);
    }

    public void saveBookReviewComment(BookReviewComment bookReviewComment) {
        bookReviewCommentRepository.save(bookReviewComment);
    }

    public void deleteBookReview(BookReview bookReview) {
        bookReviewRepository.delete(bookReview);
    }

    public void deleteBookReviewComment(BookReviewComment bookReviewComment) {
        bookReviewCommentRepository.delete(bookReviewComment);
    }

    public void incrementBookReviewUpvoteCount(BookReview bookReview, Integer incrementAmount) {
        Integer currentAmount = bookReview.getUpvoteCount();
        bookReview.setUpvoteCount(currentAmount + incrementAmount);
        saveBookReview(bookReview);
    }

    public void incrementBookReviewDownvoteCount(BookReview bookReview, Integer incrementAmount) {
        Integer currentAmount = bookReview.getDownvoteCount();
        bookReview.setDownvoteCount(currentAmount + incrementAmount);
        saveBookReview(bookReview);
    }

    public void incrementBookReviewCommentUpvoteCount(BookReviewComment bookReviewComment, Integer incrementAmount) {
        Integer currentAmount = bookReviewComment.getUpvoteCount();
        bookReviewComment.setUpvoteCount(currentAmount + incrementAmount);
        saveBookReviewComment(bookReviewComment);
    }

    public void incrementBookReviewCommentDownvoteCount(BookReviewComment bookReviewComment, Integer incrementAmount) {
        Integer currentAmount = bookReviewComment.getDownvoteCount();
        bookReviewComment.setDownvoteCount(currentAmount + incrementAmount);
        saveBookReviewComment(bookReviewComment);
    }

    public boolean canFriendReviewBook(Friend friend, Book book) {
        return friend.getLends().stream().anyMatch(lend -> lend.getBookCopy().getBook().equals(book));
    }
}
