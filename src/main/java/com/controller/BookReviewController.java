package com.controller;

import com.context.*;
import com.service.BookReviewService;
import com.service.BookService;
import com.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/book-reviews")
public class BookReviewController {
    private final BookReviewService bookReviewService;
    private final BookService bookService;
    private final FriendService friendService;

    public BookReviewController(BookReviewService service, BookService bookService, FriendService friendService) {
        this.bookReviewService = service;
        this.bookService = bookService;
        this.friendService = friendService;
    }

    @Operation(summary = "Search book reviews", operationId = "getBookReviews")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found book reviews",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = java.lang.Object[].class))}
            ),
            @ApiResponse(responseCode = "204", description = "No book reviews found")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<BookReview>> getBookReviews() {
        List<BookReview> bookReviews = bookReviewService.getBookReviews();

        if (bookReviews.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(bookReviews);
    }

    @Operation(summary = "Search reviews for a book", operationId = "getBookReviewsForBook")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found book reviews",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = java.lang.Object[].class))}
            ),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "204", description = "No book reviews found")
    })
    @GetMapping(value = "/book/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<BookReview>> getBookReviewsForBook(@PathVariable Long bookId) {
        Optional<Book> book = bookService.getBookById(bookId);

        if (book.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<BookReview> bookReviews = bookReviewService.getBookReviewsForBook(book.get());

        if (bookReviews.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(bookReviews);
    }

    @Operation(summary = "Get a book review", operationId = "getBookReview")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found book review",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = java.lang.Object.class))}
            ),
            @ApiResponse(responseCode = "404", description = "No book review found")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookReview> getBookReview(@PathVariable Long id) {
        Optional<BookReview> bookReview = bookReviewService.getBookReviewById(id);

        if (bookReview.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(bookReview.get());
    }

    @Operation(summary = "Add a book review", operationId = "addBookReview")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book review was created",
                    headers = {@Header(name = "location", schema = @Schema(type = "String"))}
            ),
            @ApiResponse(responseCode = "400", description = "Specified friend cannot review the specified book"),
            @ApiResponse(responseCode = "404", description = "Either book or friend were not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping(produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> addBookReview(@RequestBody String reviewText, @RequestParam Integer authorScore, @RequestParam Long bookId, @RequestParam Long friendId) {
        Optional<Book> book = bookService.getBookById(bookId);

        if (book.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Cannot find book with id: %d", bookId));
        }

        Optional<Friend> friend = friendService.getFriendById(friendId);

        if (friend.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Cannot find friend with id: %d", friendId));
        }

        if (!bookReviewService.canFriendReviewBook(friend.get(), book.get())) {
            return ResponseEntity.badRequest().build();
        }

        BookReview bookReview = new BookReview(reviewText, authorScore, 0, 0, book.get(), friend.get());
        bookReviewService.saveBookReview(bookReview);
        URI uri = WebMvcLinkBuilder.linkTo(this.getClass()).slash(bookReview.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Update the text of a book review", operationId = "updateBookReviewText")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book review text was updated"),
            @ApiResponse(responseCode = "500", description = "Something went wrong"),
            @ApiResponse(responseCode = "404", description = "Book review not found")
    })
    @PutMapping(value = "/{id}/text", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateBookReviewText(@PathVariable Long id, @RequestBody String reviewText) {
        Optional<BookReview> existingBookReview = bookReviewService.getBookReviewById(id);

        if (existingBookReview.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        existingBookReview.get().setReviewText(reviewText);
        bookReviewService.saveBookReview(existingBookReview.get());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update the score of a book review", operationId = "updateBookReviewText")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book review score was updated"),
            @ApiResponse(responseCode = "500", description = "Something went wrong"),
            @ApiResponse(responseCode = "404", description = "Book review not found")
    })
    @PutMapping(value = "/{id}/score", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateBookReviewScore(@PathVariable Long id, @RequestParam Integer score) {
        Optional<BookReview> existingBookReview = bookReviewService.getBookReviewById(id);

        if (existingBookReview.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        existingBookReview.get().setAuthorScore(score);
        bookReviewService.saveBookReview(existingBookReview.get());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Register book review upvote", operationId = "registerBookReviewUpvote")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book review upvote was registered"),
            @ApiResponse(responseCode = "500", description = "Something went wrong"),
            @ApiResponse(responseCode = "404", description = "Book review not found")
    })
    @PostMapping(value = "/{id}/upvote", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateBookReviewUpvote(@PathVariable Long id, @RequestParam Integer incrementAmount) {
        Optional<BookReview> existingBookReview = bookReviewService.getBookReviewById(id);

        if (existingBookReview.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        bookReviewService.incrementBookReviewUpvoteCount(existingBookReview.get(), incrementAmount);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Register book review downvote", operationId = "registerBookReviewUpvote")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book review downvote was registered"),
            @ApiResponse(responseCode = "500", description = "Something went wrong"),
            @ApiResponse(responseCode = "404", description = "Book review not found")
    })
    @PostMapping(value = "/{id}/downvote", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateBookReviewDownvote(@PathVariable Long id, @RequestParam Integer incrementAmount) {
        Optional<BookReview> existingBookReview = bookReviewService.getBookReviewById(id);

        if (existingBookReview.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        bookReviewService.incrementBookReviewDownvoteCount(existingBookReview.get(), incrementAmount);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a book review", operationId = "deleteBookReview")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book review was deleted"),
            @ApiResponse(responseCode = "500", description = "Something went wrong"),
            @ApiResponse(responseCode = "404", description = "Book review not found")
    })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteBookReview(@PathVariable Long id) {
        Optional<BookReview> existingBookReview = bookReviewService.getBookReviewById(id);

        if (existingBookReview.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        bookReviewService.deleteBookReview(existingBookReview.get());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search comments for a book review", operationId = "getBookReviewCommentsForBookReview")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found book reviews",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = java.lang.Object[].class))}
            ),
            @ApiResponse(responseCode = "404", description = "Book review not found"),
            @ApiResponse(responseCode = "204", description = "No book reviews found")
    })
    @GetMapping(value="/{id}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<BookReviewComment>> getBookReviewCommentsForBookReview(@PathVariable Long id) {
        Optional<BookReview> bookReview = bookReviewService.getBookReviewById(id);

        if (bookReview.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<BookReviewComment> bookReviewComments = bookReviewService.getBookReviewCommentsForBookReview(bookReview.get());

        if (bookReviewComments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(bookReviewComments);
    }

    @Operation(summary = "Add a comment to a book review", operationId = "addBookReviewComment")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book review comment added",
                    headers = {@Header(name = "location", schema = @Schema(type = "String"))}
            ),
            @ApiResponse(responseCode = "404", description = "Book review or friend not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping(value="/{id}/comments", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> addBookReviewComment(@PathVariable Long id, @RequestBody String commentText, @RequestParam Long friendId) {
        Optional<BookReview> bookReview = bookReviewService.getBookReviewById(id);

        if (bookReview.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Cannot find book review with id: %d", id));
        }

        Optional<Friend> friend = friendService.getFriendById(friendId);

        if (friend.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Cannot find friend with id: %d", friendId));
        }

        BookReviewComment bookReviewComment = new BookReviewComment(commentText, 0, 0, bookReview.get(), friend.get());
        bookReviewService.saveBookReviewComment(bookReviewComment);
        URI uri = WebMvcLinkBuilder.linkTo(this.getClass()).slash(id).slash("comments").slash(bookReviewComment.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
