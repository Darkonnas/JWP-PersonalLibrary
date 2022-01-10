package com.repository;

import com.context.BookReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReviewCommentRepository extends JpaRepository<BookReviewComment, Long> {
}
