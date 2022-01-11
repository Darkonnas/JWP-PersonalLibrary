package com.context;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "book_review_comment")
public class BookReviewComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "comment_text", length = 1000, nullable = false)
    private String commentText;

    @Column(name = "upvote_count", nullable = false)
    private int upvoteCount;

    @Column(name = "downvote_count", nullable = false)
    private int downvoteCount;

    @ManyToOne
    @JsonBackReference("review")
    @JoinColumn(name = "review_id", nullable = false)
    private BookReview review;

    @ManyToOne
    @JsonBackReference("author")
    @JoinColumn(name = "author_id", nullable = false)
    private Friend author;

    public BookReviewComment() {
    }

    public BookReviewComment(long id, String commentText, int upvoteCount, int downvoteCount, BookReview review, Friend author) {
        this.id = id;
        this.commentText = commentText;
        this.upvoteCount = upvoteCount;
        this.downvoteCount = downvoteCount;
        this.review = review;
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public String getCommentText() {
        return commentText;
    }

    public int getUpvoteCount() {
        return upvoteCount;
    }

    public int getDownvoteCount() {
        return downvoteCount;
    }

    public BookReview getReview() {
        return review;
    }

    public Friend getAuthor() {
        return author;
    }
}
