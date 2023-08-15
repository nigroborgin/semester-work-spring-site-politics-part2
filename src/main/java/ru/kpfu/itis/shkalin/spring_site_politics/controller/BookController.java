package ru.kpfu.itis.shkalin.spring_site_politics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.book.BookFormDto;
import ru.kpfu.itis.shkalin.spring_site_politics.security.CustomUserDetails;
import ru.kpfu.itis.shkalin.spring_site_politics.service.db.BookService;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public String showAll(
            @AuthenticationPrincipal CustomUserDetails userSess,
            ModelMap map) {

        bookService.showAll(userSess, map);
        return "/books/book-list";
    }

    @GetMapping("/{id}")
    public String showOne(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable(required = false) Optional<Integer> id,
            ModelMap map) {

        bookService.showOne(userSess, id, map);
        return "/books/book";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/new")
    public String add(
            @AuthenticationPrincipal CustomUserDetails userSess,
            ModelMap map) {

        bookService.showNewForm(userSess, map);
        return "/books/book-form";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/edit")
    public String edit(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable(required = false) Optional<Integer> id,
            ModelMap map) {

        bookService.showEditForm(userSess, id, map);
        return "/books/book-form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/new")
    public String addHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @ModelAttribute("bookInfo") BookFormDto bookFormDto,
            @RequestParam("file") MultipartFile bookFile,
            BindingResult result,
            ModelMap map) throws IOException {

        bookService.create(userSess, bookFormDto, bookFile);
        return "redirect:/books";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/edit")
    public String editHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @ModelAttribute("bookInfo") BookFormDto bookFormDto,
            @RequestParam("file") MultipartFile bookFile,
            @PathVariable Optional<Integer> id,
            BindingResult result,
            ModelMap map) throws IOException {

        bookService.update(userSess, bookFormDto, bookFile, id);
        return "redirect:/books";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/delete")
    public String deleteHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable Optional<Integer> id) {

        bookService.delete(userSess, id);
        return "redirect:/books";
    }

}
