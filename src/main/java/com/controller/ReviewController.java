package com.controller;

import com.ReviewManagementApplication;
import org.springframework.stereotype.Controller;

@Controller
public class ReviewController {
    private final ReviewManagementApplication reviewManagementApplication;

    public ReviewController(ReviewManagementApplication reviewManagementApplication) {
        this.reviewManagementApplication = reviewManagementApplication;
    }
}
