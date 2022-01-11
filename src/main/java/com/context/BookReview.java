package com.context;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Entity
@Table(name = "book_review")
public class BookReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "review_text", length = 1000, nullable = false)
    private String reviewText;

    @Column(name = "author_score", nullable = false)
    @Min(1)
    @Max(10)
    private int authorScore;

    @Column(name = "upvote_count", nullable = false)
    private int upvoteCount;

    @Column(name = "downvote_count", nullable = false)
    private int downvoteCount;

    @ManyToOne
    @JsonBackReference("book")
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JsonBackReference("author")
    @JoinColumn(name = "author_id", nullable = false)
    private Friend author;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "review")
    private List<BookReviewComment> comments;

    public BookReview() {}

    public BookReview(long id, String reviewText, int authorScore, int upvoteCount, int downvoteCount, Book book, Friend author, List<BookReviewComment> comments) {
        this.id = id;
        this.reviewText = reviewText;
        this.authorScore = authorScore;
        this.upvoteCount = upvoteCount;
        this.downvoteCount = downvoteCount;
        this.book = book;
        this.author = author;
        this.comments = comments;
    }

    public long getId() {
        return id;
    }

    public String getReviewText() {
        return reviewText;
    }

    public int getAuthorScore() {
        return authorScore;
    }

    public int getUpvoteCount() {
        return upvoteCount;
    }

    public int getDownvoteCount() {
        return downvoteCount;
    }

    public Book getBook() {
        return book;
    }

    public Friend getAuthor() {
        return author;
    }

    public List<BookReviewComment> getComments() {
        return comments;
    }
}
