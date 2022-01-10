package com.service;

import com.repository.BookReviewCommentRepository;
import com.repository.BookReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class BookReviewService {
    private final BookReviewRepository bookReviewRepository;
    private final BookReviewCommentRepository bookReviewCommentRepository;

    public BookReviewService(BookReviewRepository bookReviewRepository, BookReviewCommentRepository bookReviewCommentRepository) {
        this.bookReviewRepository = bookReviewRepository;
        this.bookReviewCommentRepository = bookReviewCommentRepository;
    }
}
