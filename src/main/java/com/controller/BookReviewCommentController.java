package com.controller;

import com.context.BookReviewComment;
import com.service.BookReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/book-review-comments")
public class BookReviewCommentController {
    private final BookReviewService bookReviewService;

    public BookReviewCommentController(BookReviewService bookReviewService) {
        this.bookReviewService = bookReviewService;
    }

    @Operation(summary = "Update the text of a book review comment", operationId = "updateBookReviewCommentText")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book review comment text was updated"),
            @ApiResponse(responseCode = "500", description = "Something went wrong"),
            @ApiResponse(responseCode = "404", description = "Book review comment not found")
    })
    @PutMapping(value= "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateBookReviewCommentText(@PathVariable Long id, @RequestBody String commentText) {
        Optional<BookReviewComment> existingBookReviewComment = bookReviewService.getBookReviewCommentById(id);

        if (existingBookReviewComment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Could not find book review comment with id: %d", id));
        }

        existingBookReviewComment.get().setCommentText(commentText);
        bookReviewService.saveBookReviewComment(existingBookReviewComment.get());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Register book review comment upvote", operationId = "registerBookReviewCommentUpvote")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book review comment upvote was registered"),
            @ApiResponse(responseCode = "500", description = "Something went wrong"),
            @ApiResponse(responseCode = "404", description = "Book review or comment not found")
    })
    @PostMapping(value = "/{id}/upvote", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateBookReviewCommentUpvote(@PathVariable Long id, @RequestParam Integer incrementAmount) {
        Optional<BookReviewComment> existingBookReviewComment = bookReviewService.getBookReviewCommentById(id);

        if (existingBookReviewComment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Could not find book review comment with id: %d", id));
        }

        bookReviewService.incrementBookReviewCommentUpvoteCount(existingBookReviewComment.get(), incrementAmount);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Register book review comment downvote", operationId = "registerBookReviewCommentDownvote")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book review comment downvote was registered"),
            @ApiResponse(responseCode = "500", description = "Something went wrong"),
            @ApiResponse(responseCode = "404", description = "Book review or comment not found")
    })
    @PostMapping(value = "/{id}/downvote", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateBookReviewCommentDownvote(@PathVariable Long id, @RequestParam Integer incrementAmount) {
        Optional<BookReviewComment> existingBookReviewComment = bookReviewService.getBookReviewCommentById(id);

        if (existingBookReviewComment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Could not find book review comment with id: %d", id));
        }

        bookReviewService.incrementBookReviewCommentDownvoteCount(existingBookReviewComment.get(), incrementAmount);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a book review comment", operationId = "deleteBookReviewComment")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book review comment was deleted"),
            @ApiResponse(responseCode = "500", description = "Something went wrong"),
            @ApiResponse(responseCode = "404", description = "Book review or comment not found")
    })
    @DeleteMapping(value= "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteBookReviewComment(@PathVariable Long id) {
        Optional<BookReviewComment> existingBookReviewComment = bookReviewService.getBookReviewCommentById(id);

        if (existingBookReviewComment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Could not find book review comment with id: %d", id));
        }

        bookReviewService.deleteBookReviewComment(existingBookReviewComment.get());
        return ResponseEntity.noContent().build();
    }
}
