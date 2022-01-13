package com.controller;

import com.context.Author;
import com.service.BookService;
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
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    private final BookService service;

    public AuthorController(BookService service) {
        this.service = service;
    }

    @Operation(summary = "Search authors", operationId = "getAuthors")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found authors",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = java.lang.Object[].class))}
            ),
            @ApiResponse(responseCode = "204", description = "No authors found")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Author>> getAuthors() {
        List<Author> authors = service.getAuthors();

        if (authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(authors);
    }

    @Operation(summary = "Get an author", operationId = "getAuthor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found author",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = java.lang.Object.class))}
            ),
            @ApiResponse(responseCode = "404", description = "No author found")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> getAuthor(@PathVariable Long id) {
        Optional<Author> author = service.getAuthorById(id);

        if (author.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(author.get());
    }

    @Operation(summary = "Create an author", operationId = "addAuthor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Author was created",
                    headers = {@Header(name = "location", schema = @Schema(type = "String"))}
            ),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping(value = "", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addAuthor(@RequestBody Author author) {
        service.saveAuthor(author);
        URI uri = WebMvcLinkBuilder.linkTo(this.getClass()).slash(author.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Update an author", operationId = "updateAuthor")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Author was updated"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateAuthor(@PathVariable Long id, @RequestBody Author author) {
        Optional<Author> existingAuthor = service.getAuthorById(id);

        if (existingAuthor.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        existingAuthor.get().updateAuthor(author);
        service.saveAuthor(existingAuthor.get());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete an author", operationId = "deleteAuthor")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Author was deleted"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        Optional<Author> existingAuthor = service.getAuthorById(id);

        if (existingAuthor.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.deleteAuthor(existingAuthor.get());
        return ResponseEntity.noContent().build();
    }
}
