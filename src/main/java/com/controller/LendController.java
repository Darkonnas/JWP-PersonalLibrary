package com.controller;

import com.context.*;
import com.service.BookService;
import com.service.FriendService;
import com.service.LendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lends")
public class LendController {
    private final LendService lendService;
    private final BookService bookService;
    private final FriendService friendService;

    public LendController(LendService lendService, BookService bookService, FriendService friendService) {
        this.lendService = lendService;
        this.bookService = bookService;
        this.friendService = friendService;
    }

    @Operation(summary = "Search lends", operationId = "getLends")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found lends",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = java.lang.Object[].class))}
            ),
            @ApiResponse(responseCode = "204", description = "No lends found")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Lend>> getAllLends() {
        List<Lend> lends = lendService.getLends();

        if (lends.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lends);
    }

    @Operation(summary = "Get a lend", operationId = "getLend")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found lend",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = java.lang.Object.class))}
            ),
            @ApiResponse(responseCode = "404", description = "No lend found")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Lend> getLend(@PathVariable Long id) {
        Optional<Lend> lend = lendService.getLendById(id);

        if (lend.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(lend.get());
    }

    @Operation(summary = "Get the friend that last borrower a book copy", operationId = "getLastBorrowerOfBookCopy")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found last borrower",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = java.lang.Object.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Book copy was not found"),
            @ApiResponse(responseCode = "404", description = "Book copy was never lent")
    })
    @GetMapping(value = "/last-borrower/{bookCopyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Friend> getLastBorrowerOfBookCopy(@PathVariable Long bookCopyId) {
        Optional<BookCopy> bookCopy = bookService.getBookCopyById(bookCopyId);

        if (bookCopy.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Lend> lastLend = lendService.getLastLendOfBookCopy(bookCopy.get());

        if (lastLend.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(lastLend.get().getFriend());
    }

    @Operation(summary = "Create a lend", operationId = "addLend")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Lend was created",
                    headers = {@Header(name = "location", schema = @Schema(type = "String"))}
            ),
            @ApiResponse(responseCode = "400", description = "Cannot lend the desired book to the specified friend"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping(produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> addLend(@RequestParam Long bookId, @RequestParam Long friendId, @RequestParam Integer lendDuration) {
        Optional<Book> book = bookService.getBookById(bookId);

        if (book.isEmpty()) {
            return ResponseEntity.badRequest().body(String.format("Cannot find book with id: %d", bookId));
        }

        Optional<BookCopy> bookCopyAvailableForLending = lendService.getAvailableCopyForLending(book.get());

        if (bookCopyAvailableForLending.isEmpty()) {
            return ResponseEntity.badRequest().body(String.format("There are no available copies to be lend for book with id: %d", bookId));
        }

        Optional<Friend> friend = friendService.getFriendById(friendId);

        if (friend.isEmpty()) {
            return ResponseEntity.badRequest().body(String.format("Cannot find friend with id: %d", friendId));
        }

        if (!lendService.canFriendBorrowBooks(friend.get())) {
            return ResponseEntity.badRequest().body(String.format("Friend with id %d is currently not allowed to borrow (more) books", friendId));
        }

        Lend lend = new Lend(bookCopyAvailableForLending.get(), friend.get(), LocalDateTime.now(ZoneOffset.UTC), lendDuration, Lend.LendStatus.LENT);
        lendService.saveLend(lend);
        URI uri = WebMvcLinkBuilder.linkTo(this.getClass()).slash(lend.getId()).toUri();
        bookCopyAvailableForLending.get().setShelf(null);
        bookService.saveBookCopy(bookCopyAvailableForLending.get());
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Update lend status", operationId = "updateLendStatus")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Lend status was updated"),
            @ApiResponse(responseCode = "404", description = "Existing lend not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PutMapping(value = "/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateLendStatus(@PathVariable Long id, @RequestParam Lend.LendStatus status) {
        Optional<Lend> existingLend = lendService.getLendById(id);

        if (existingLend.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        existingLend.get().setLendStatus(status);
        lendService.saveLend(existingLend.get());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search extensions for lend", operationId = "getLendExtensionsForLend")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found lend extensions",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = java.lang.Object[].class))}
            ),
            @ApiResponse(responseCode = "204", description = "No lend extensions found"),
            @ApiResponse(responseCode = "404", description = "Lend not found"),
    })
    @GetMapping(value = "/{id}/extensions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<LendExtension>> getLendExtensionsForLend(@PathVariable Long id) {
        Optional<Lend> existingLend = lendService.getLendById(id);

        if (existingLend.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<LendExtension> lendExtensions = existingLend.get().getLendExtensions();

        if (lendExtensions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lendExtensions);
    }

    @Operation(summary = "Request extension for a lend", operationId = "requestLendExtension")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Lend extension requested",
                    headers = {@Header(name = "location", schema = @Schema(type = "String"))}
            ),
            @ApiResponse(responseCode = "400", description = "Cannot request an(y more) extension for the specified lend"),
            @ApiResponse(responseCode = "404", description = "Lend not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping(value = "/{id}/extensions", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> requestLendExtension(@PathVariable Long id, @RequestBody String reason, @RequestParam Integer extensionDuration) {
        Optional<Lend> existingLend = lendService.getLendById(id);

        if (existingLend.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!lendService.canExtensionBeRequestedForLend(existingLend.get())) {
            return ResponseEntity.badRequest().body(String.format("Cannot request an extension for lend with id: %d", id));
        }

        LendExtension extension = new LendExtension(reason, extensionDuration, LocalDateTime.now(ZoneOffset.UTC), LendExtension.LendExtensionStatus.PENDING, existingLend.get());
        lendService.saveLendExtension(extension);
        URI uri = WebMvcLinkBuilder.linkTo(this.getClass()).slash(id).slash("extensions").slash(extension.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
