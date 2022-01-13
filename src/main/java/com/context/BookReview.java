package com.context;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Entity
@Table(name = "book_review")
public class BookReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "review_text", length = 1000, nullable = false)
    private String reviewText;

    @Column(name = "author_score", nullable = false)
    @Min(1)
    @Max(10)
    private Integer authorScore;

    @Column(name = "upvote_count", nullable = false)
    private Integer upvoteCount;

    @Column(name = "downvote_count", nullable = false)
    private Integer downvoteCount;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Friend author;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "review")
    @JsonIgnore
    private List<BookReviewComment> comments;

    public BookReview() {}

    public BookReview(String reviewText, Integer authorScore, Integer upvoteCount, Integer downvoteCount, Book book, Friend author) {
        this.reviewText = reviewText;
        this.authorScore = authorScore;
        this.upvoteCount = upvoteCount;
        this.downvoteCount = downvoteCount;
        this.book = book;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public String getReviewText() {
        return reviewText;
    }

    public Integer getAuthorScore() {
        return authorScore;
    }

    public Integer getUpvoteCount() {
        return upvoteCount;
    }

    public Integer getDownvoteCount() {
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

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public void setAuthorScore(Integer authorScore) {
        this.authorScore = authorScore;
    }

    public void setUpvoteCount(Integer upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    public void setDownvoteCount(Integer downvoteCount) {
        this.downvoteCount = downvoteCount;
    }

    public void setComments(List<BookReviewComment> comments) {
        this.comments = comments;
    }
}
