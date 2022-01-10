package com;

import com.service.BookReviewService;
import org.springframework.stereotype.Service;

@Service
public class ReviewManagementApplication {
    private final BookReviewService bookReviewService;

    public ReviewManagementApplication(BookReviewService bookReviewService) {
        this.bookReviewService = bookReviewService;
    }
}
