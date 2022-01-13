package com.controller;

import com.context.Book;
import com.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/library-management")
public class LibraryManagementController {
    private final BookService bookService;

    public LibraryManagementController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Obtain current library distribution of book copies", operationId = "getLibraryBookCopiesLayout")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found books",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = java.lang.Object[].class))}
            ),
            @ApiResponse(responseCode = "204", description = "There are currently no book copies in the library")
    })
    @GetMapping("/layout")
    public ResponseEntity<Map<Integer, Map<Integer, Map<Integer, List<Book>>>>> getLibraryBookCopiesLayout() {
        var layout = bookService.getLibraryBookCopiesLayout();

        if (layout.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(layout);
    }
}
