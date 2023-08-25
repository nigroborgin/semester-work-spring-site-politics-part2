package ru.kpfu.itis.shkalin.spring_site_politics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping(value = {"/", "/main"})
    public String showMainPage() {
        return "/main";
    }

    @GetMapping("/favicon.ico")
    public String icon() {
        return "redirect:/static/picture/favicon.ico";
    }

}
