package com.controller;

import com.LendManagementApplication;
import org.springframework.stereotype.Controller;

@Controller
public class LendController {
    private final LendManagementApplication lendManagementApplication;

    public LendController(LendManagementApplication lendManagementApplication) {
        this.lendManagementApplication = lendManagementApplication;
    }
}
