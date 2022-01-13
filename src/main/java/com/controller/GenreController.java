package com.controller;

import com.context.Genre;
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
@RequestMapping("/api/genres")
public class GenreController {
    private final BookService service;

    public GenreController(BookService service) {
        this.service = service;
    }

    @Operation(summary = "Search genres", operationId = "getGenres")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found genres",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = java.lang.Object[].class))}
            ),
            @ApiResponse(responseCode = "204", description = "No genres found")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Genre>> getGenres() {
        List<Genre> genres = service.getGenres();

        if (genres.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(genres);
    }

    @Operation(summary = "Get an genre", operationId = "getGenre")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found genre",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = java.lang.Object.class))}
            ),
            @ApiResponse(responseCode = "404", description = "No genre found")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Genre> getGenre(@PathVariable Long id) {
        Optional<Genre> genre = service.getGenreById(id);

        if (genre.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(genre.get());
    }

    @Operation(summary = "Create an genre", operationId = "addGenre")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Genre was created",
                    headers = {@Header(name = "location", schema = @Schema(type = "String"))}
            ),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping(value = "", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addGenre(@RequestBody Genre genre) {
        service.saveGenre(genre);
        URI uri = WebMvcLinkBuilder.linkTo(this.getClass()).slash(genre.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Update an genre", operationId = "updateGenre")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Genre was updated"),
            @ApiResponse(responseCode = "500", description = "Something went wrong"),
            @ApiResponse(responseCode = "404", description = "Genre not found")
    })
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateGenre(@PathVariable Long id, @RequestBody Genre genre) {
        Optional<Genre> existingGenre = service.getGenreById(id);

        if (existingGenre.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        existingGenre.get().updateGenre(genre);
        service.saveGenre(existingGenre.get());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete an genre", operationId = "deleteGenre")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Genre was deleted"),
            @ApiResponse(responseCode = "500", description = "Something went wrong"),
            @ApiResponse(responseCode = "404", description = "Genre not found")
    })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        Optional<Genre> existingGenre = service.getGenreById(id);

        if (existingGenre.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.deleteGenre(existingGenre.get());
        return ResponseEntity.noContent().build();
    }
}
