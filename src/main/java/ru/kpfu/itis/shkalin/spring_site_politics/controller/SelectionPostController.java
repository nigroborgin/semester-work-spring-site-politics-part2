package ru.kpfu.itis.shkalin.spring_site_politics.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/selections-posts")
public class SelectionPostController {

    // общие подборки постов
    @GetMapping("/posts")
    public String showPostSelections() {

        return "/main";
    }

    // личные подборки постов
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my-posts")
    public String showMyPostSelections() {

        return "/main";
    }

}
