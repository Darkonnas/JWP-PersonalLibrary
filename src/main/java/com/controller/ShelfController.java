package com.controller;

import com.context.Shelf;
import com.service.ShelfService;
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
@RequestMapping("/api/shelves")
public class ShelfController {
    private final ShelfService service;

    public ShelfController(ShelfService service) {
        this.service = service;
    }

    @Operation(summary = "Search shelves", operationId = "getShelves")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found shelves",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = java.lang.Object[].class))}
            ),
            @ApiResponse(responseCode = "204", description = "No shelves found")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Shelf>> getShelves() {
        List<Shelf> shelves = service.getShelves();

        if (shelves.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(shelves);
    }

    @Operation(summary = "Get an shelf", operationId = "getShelf")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found shelf",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = java.lang.Object.class))}
            ),
            @ApiResponse(responseCode = "404", description = "No shelf found")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Shelf> getShelf(@PathVariable Long id) {
        Optional<Shelf> shelf = service.getShelfById(id);

        if (shelf.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(shelf.get());
    }

    @Operation(summary = "Create an shelf", operationId = "addShelf")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Shelf was created",
                    headers = {@Header(name = "location", schema = @Schema(type = "String"))}
            ),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping(value = "", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addShelf(@RequestBody Shelf shelf) {
        service.saveShelf(shelf);
        URI uri = WebMvcLinkBuilder.linkTo(this.getClass()).slash(shelf.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Update an shelf", operationId = "updateShelf")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Shelf was updated"),
            @ApiResponse(responseCode = "404", description = "Shelf not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateShelf(@PathVariable Long id, @RequestBody Shelf shelf) {
        Optional<Shelf> existingShelf = service.getShelfById(id);

        if (existingShelf.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        existingShelf.get().updateShelf(shelf);
        service.saveShelf(existingShelf.get());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete an shelf", operationId = "deleteShelf")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Shelf was deleted"),
            @ApiResponse(responseCode = "404", description = "Shelf not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteShelf(@PathVariable Long id) {
        Optional<Shelf> existingShelf = service.getShelfById(id);

        if (existingShelf.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.deleteShelf(existingShelf.get());
        return ResponseEntity.noContent().build();
    }
}
