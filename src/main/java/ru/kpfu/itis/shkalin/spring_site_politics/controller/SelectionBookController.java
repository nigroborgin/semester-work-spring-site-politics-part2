package ru.kpfu.itis.shkalin.spring_site_politics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.selections_book.SelectionBookFormDto;
import ru.kpfu.itis.shkalin.spring_site_politics.security.CustomUserDetails;
import ru.kpfu.itis.shkalin.spring_site_politics.service.db.SelectionBookService;

import java.util.Optional;

@Controller
@RequestMapping("/selections-books")
public class SelectionBookController {

    @Autowired
    private SelectionBookService selectionBookService;

    @GetMapping
    public String showAll(
            @AuthenticationPrincipal CustomUserDetails userSess,
            ModelMap map) {

        selectionBookService.showAll(userSess, map);
        return "selections/book-selections-list";
    }

    @GetMapping("/{id}")
    public String showOne(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable(required = false) Optional<Integer> id,
            ModelMap map) {

        selectionBookService.showOne(userSess, id, map);
        return "selections/book-selection";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String add(
            @AuthenticationPrincipal CustomUserDetails userSess,
            ModelMap map) {

        selectionBookService.showNewForm(userSess, map);
        return "selections/book-selections-form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String edit(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable(required = false) Optional<Integer> id,
            ModelMap map) {

        selectionBookService.showEditForm(userSess, id, map);
        return "selections/book-selections-form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public String addHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @ModelAttribute("selectionBookInfo") SelectionBookFormDto selectionBookFormDto,
            BindingResult result,
            ModelMap map) {

        selectionBookService.create(userSess, selectionBookFormDto);
        return "redirect:/selections-books";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/edit")
    public String editHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @ModelAttribute("selectionBookInfo") SelectionBookFormDto selectionBookFormDto,
            @PathVariable(required = false) Optional<Integer> id) {

        selectionBookService.update(userSess, selectionBookFormDto, id);
        return "redirect:/selections-books";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String deleteHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable(required = false) Optional<Integer> id) {

        selectionBookService.delete(userSess, id);
        return "redirect:/selections-books";
    }



    // личные подборки книг
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my")
    public String showAllMy() {

        return "/main";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my/{id}")
    public String showOneMy(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable(required = false) Optional<Integer> id,
            ModelMap map) {

        return "/main";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my/new")
    public String addMy(
            @AuthenticationPrincipal CustomUserDetails userSess,
            ModelMap map) {

        return "/main";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my/{id}/edit")
    public String editMy(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable(required = false) Optional<Integer> id,
            ModelMap map) {

        return "/main";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/my/new")
    public String addMyHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @ModelAttribute("selectionBookInfo") SelectionBookFormDto selectionBookFormDto,
            BindingResult result,
            ModelMap map) {

        return "/main";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/my/{id}/edit")
    public String editMyHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @ModelAttribute("selectionBookInfo") SelectionBookFormDto selectionBookFormDto,
            @PathVariable(required = false) Optional<Integer> id) {

        return "/main";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/my/{id}/delete")
    public String deleteMyHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable(required = false) Optional<Integer> id) {

        return "/main";
    }

}
