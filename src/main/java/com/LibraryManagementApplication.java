package com;

import com.service.*;
import org.springframework.stereotype.Service;

@Service
public class LibraryManagementApplication {
    private final AuthorService authorService;
    private final BookService bookService;
    private final FriendService friendService;
    private final GenreService genreService;
    private final LendService lendService;
    private final ShelfService shelfService;

    public LibraryManagementApplication(AuthorService authorService, BookService bookService, FriendService friendService, GenreService genreService, LendService lendService, ShelfService shelfService) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.friendService = friendService;
        this.genreService = genreService;
        this.lendService = lendService;
        this.shelfService = shelfService;
    }
}
