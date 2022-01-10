package com;

import com.service.*;
import org.springframework.stereotype.Service;

@Service
public class LibraryManagementApplication {
    private final AuthorService authorService;
    private final BookService bookService;
    private final GenreService genreService;
    private final ShelfService shelfService;

    public LibraryManagementApplication(AuthorService authorService, BookService bookService, GenreService genreService, ShelfService shelfService) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.genreService = genreService;
        this.shelfService = shelfService;
    }
}
