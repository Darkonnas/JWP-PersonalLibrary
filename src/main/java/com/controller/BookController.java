package com.controller;

import com.context.Author;
import com.context.Book;
import com.context.BookCopy;
import com.context.Genre;
import com.service.BookService;
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
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService service) {
        this.bookService = service;
    }

    @Operation(summary = "Search books", operationId = "getBooks")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found books",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = java.lang.Object[].class))}
            ),
            @ApiResponse(responseCode = "204", description = "No books found")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Book>> getAllBooks() {
        List<Book> books = bookService.getBooks();

        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Get a book", operationId = "getBook")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found book",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = java.lang.Object.class))}
            ),
            @ApiResponse(responseCode = "404", description = "No book found")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);

        if (book.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(book.get());
    }

    @Operation(summary = "Search book copies of a book", operationId = "getBookCopies")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found book copies of specified book",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = java.lang.Object[].class))}
            ),
            @ApiResponse(responseCode = "204", description = "No books copies found for the specified book"),
            @ApiResponse(responseCode = "404", description = "Specified book not found")
    })
    @GetMapping(value = "/{id}/copies", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<BookCopy>> getBookCopies(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);

        if (book.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<BookCopy> copies = book.get().getCopies();

        if (copies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(copies);
    }

    @Operation(summary = "Create a book", operationId = "addBook")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book was created",
                    headers = {@Header(name = "location", schema = @Schema(type = "String"))}
            ),
            @ApiResponse(responseCode = "400", description = "Either author or genre were not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping(produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> addBook(@RequestParam String title,
                                          @RequestBody(required = false) String description,
                                          @RequestParam(required = false) Integer numberOfPages,
                                          @RequestParam Long authorId,
                                          @RequestParam Long genreId) {
        Optional<Author> author = bookService.getAuthorById(authorId);

        if (author.isEmpty()) {
            return ResponseEntity.badRequest().body(String.format("Cannot find author with id: %d", authorId));
        }

        Optional<Genre> genre = bookService.getGenreById(genreId);

        if (genre.isEmpty()) {
            return ResponseEntity.badRequest().body(String.format("Cannot find genre with id: %d", genreId));
        }

        Book book = new Book(title, description, numberOfPages, author.get(), genre.get());
        bookService.saveBook(book);
        URI uri = WebMvcLinkBuilder.linkTo(this.getClass()).slash(book.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Add a copy for a book", operationId = "addBookCopy")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book copy was added",
                    headers = {@Header(name = "location", schema = @Schema(type = "String"))}
            ),
            @ApiResponse(responseCode = "404", description = "Specified book not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping(value = "/{id}/copies", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> addBookCopy(@PathVariable Long id, @RequestParam BookCopy.BookCondition bookCondition) {
        Optional<Book> book = bookService.getBookById(id);

        if (book.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Cannot find book with id: %s", id));
        }

        BookCopy bookCopy = new BookCopy(bookCondition, book.get());
        bookService.addBookCopy(bookCopy);
        URI uri = WebMvcLinkBuilder.linkTo(this.getClass()).slash(id).slash("copies").slash(bookCopy.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Update a book", operationId = "updateBook")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book was updated"),
            @ApiResponse(responseCode = "400", description = "Specified author or genre were not found"),
            @ApiResponse(responseCode = "404", description = "Existing book not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateBook(@PathVariable Long id,
                                             @RequestParam String title,
                                             @RequestBody(required = false) String description,
                                             @RequestParam(required = false) Integer numberOfPages,
                                             @RequestParam Long authorId,
                                             @RequestParam Long genreId) {
        Optional<Book> existingBook = bookService.getBookById(id);

        if (existingBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Author> author = bookService.getAuthorById(authorId);

        if (author.isEmpty()) {
            return ResponseEntity.badRequest().body(String.format("Cannot find author with id: %d", authorId));
        }

        Optional<Genre> genre = bookService.getGenreById(genreId);

        if (genre.isEmpty()) {
            return ResponseEntity.badRequest().body(String.format("Cannot find genre with id: %d", genreId));
        }

        existingBook.get().updateBook(title, description, numberOfPages, author.get(), genre.get());
        bookService.saveBook(existingBook.get());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a book", operationId = "deleteBook")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book was deleted"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        Optional<Book> existingBook = bookService.getBookById(id);

        if (existingBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        bookService.deleteBook(existingBook.get());
        return ResponseEntity.noContent().build();
    }
}
