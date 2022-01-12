package com.context;

import javax.persistence.*;

@Entity
@Table(name = "book_review_comment")
public class BookReviewComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment_text", length = 1000, nullable = false)
    private String commentText;

    @Column(name = "upvote_count", nullable = false)
    private Integer upvoteCount;

    @Column(name = "downvote_count", nullable = false)
    private Integer downvoteCount;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private BookReview review;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Friend author;

    public BookReviewComment() {
    }

    public BookReviewComment(String commentText, Integer upvoteCount, Integer downvoteCount, BookReview review, Friend author) {
        this.commentText = commentText;
        this.upvoteCount = upvoteCount;
        this.downvoteCount = downvoteCount;
        this.review = review;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public String getCommentText() {
        return commentText;
    }

    public Integer getUpvoteCount() {
        return upvoteCount;
    }

    public Integer getDownvoteCount() {
        return downvoteCount;
    }

    public BookReview getReview() {
        return review;
    }

    public Friend getAuthor() {
        return author;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setUpvoteCount(Integer upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    public void setDownvoteCount(Integer downvoteCount) {
        this.downvoteCount = downvoteCount;
    }
}
