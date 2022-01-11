package com.repository;

import com.context.BookReview;
import com.context.BookReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookReviewCommentRepository extends JpaRepository<BookReviewComment, Long> {
    List<BookReviewComment> findAllByReview(BookReview bookReview);
}
