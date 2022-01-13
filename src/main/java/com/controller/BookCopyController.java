package com.controller;

import com.context.BookCopy;
import com.context.Shelf;
import com.service.BookService;
import com.service.LendService;
import com.service.ShelfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/book-copies")
public class BookCopyController {
    private final BookService bookService;
    private final ShelfService shelfService;
    private final LendService lendService;

    public BookCopyController(BookService bookService, ShelfService shelfService, LendService lendService) {
        this.bookService = bookService;
        this.shelfService = shelfService;
        this.lendService = lendService;
    }

    @Operation(summary = "Update the condition of a book copy", operationId = "updateBookCopyBookCondition")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book copy status was updated"),
            @ApiResponse(responseCode = "400", description = "Book copy is currently being lend"),
            @ApiResponse(responseCode = "404", description = "Book copy not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong"),
    })
    @PutMapping(value = "/{id}/condition", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateBookCopyBookCondition(@PathVariable Long id, @RequestParam BookCopy.BookCondition bookCondition) {
        Optional<BookCopy> existingBookCopy = bookService.getBookCopyById(id);

        if (existingBookCopy.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (lendService.isBookCurrentlyLend(existingBookCopy.get())) {
            return ResponseEntity.badRequest().build();
        }

        existingBookCopy.get().setBookCondition(bookCondition);
        bookService.addBookCopy(existingBookCopy.get());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update the shelf of a book copy", operationId = "updateBookCopyShelf")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book copy status was updated"),
            @ApiResponse(responseCode = "400", description = "Book copy is currently being lent or it cannot be stored on the desired shelf"),
            @ApiResponse(responseCode = "404", description = "Book copy not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong"),
    })
    @PutMapping(value = "/{id}/shelf", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateBookCopyShelf(@PathVariable Long id, @RequestParam Long shelfId) {
        Optional<BookCopy> existingBookCopy = bookService.getBookCopyById(id);

        if (existingBookCopy.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Cannot find book copy with id: %s", id));
        }

        if (existingBookCopy.get().getShelf() == null && lendService.isBookCurrentlyLend(existingBookCopy.get())) {
            return ResponseEntity.badRequest().body("Book copy is current being lent");
        }

        Optional<Shelf> shelf = shelfService.getShelfById(shelfId);

        if (shelf.isEmpty()) {
            return ResponseEntity.badRequest().body(String.format("Cannot find shelf copy with id: %s", shelfId));
        }

        if (!shelfService.canShelfStoreBookCopy(shelf.get(), existingBookCopy.get())) {
            return ResponseEntity.badRequest().body("Book copy cannot be stored on the desired shelf");
        }

        existingBookCopy.get().setShelf(shelf.get());
        bookService.addBookCopy(existingBookCopy.get());
        return ResponseEntity.noContent().build();
    }
}
