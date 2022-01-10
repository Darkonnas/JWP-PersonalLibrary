package com.controller;

import com.LibraryManagementApplication;
import org.springframework.stereotype.Controller;

@Controller
public class LibraryController {
    private final LibraryManagementApplication libraryManagementApplication;

    public LibraryController(LibraryManagementApplication libraryManagementApplication) {
        this.libraryManagementApplication = libraryManagementApplication;
    }


}
