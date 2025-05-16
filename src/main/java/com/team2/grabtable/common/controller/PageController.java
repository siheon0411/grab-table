package com.team2.grabtable.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/register")
    public String register() {
        return "/register.html";
    }

    @GetMapping("/login")
    public String login() {
        return "/login.html";
    }

    @GetMapping("/logout")
    public String logout() {
        return "/login.html";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "/dashboard.html";
    }

    @GetMapping("/store-register")
    public String storeRegister() {
        return "/store-register.html";
    }
}
