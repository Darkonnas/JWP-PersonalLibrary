package com.repository;

import com.context.Book;
import com.context.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
    List<BookReview> findAllByBook(Book book);
}
