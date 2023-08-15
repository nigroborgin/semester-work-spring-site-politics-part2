package ru.kpfu.itis.shkalin.spring_site_politics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.post.PostFormDto;
import ru.kpfu.itis.shkalin.spring_site_politics.security.CustomUserDetails;
import ru.kpfu.itis.shkalin.spring_site_politics.service.db.PostService;

import java.util.Optional;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public String showAll(
            @AuthenticationPrincipal CustomUserDetails userSess,
            ModelMap map) {

        postService.showAll(userSess, map);
        return "/posts/post-list";
    }

    @GetMapping("/{id}")
    public String showOne(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable(required = false) Optional<Integer> id,
            ModelMap map) {

        postService.showOne(userSess, id, map);
        return "/posts/post";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/new")
    public String add(
            @AuthenticationPrincipal CustomUserDetails userSess,
            ModelMap map) {

        postService.showNewForm(userSess, map);
        return "/posts/post-form";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/edit")
    public String edit(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable(required = false) Optional<Integer> id,
            ModelMap map) {

        postService.showEditForm(userSess, id, map);
        return "/posts/post-form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/new")
    public String addHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @ModelAttribute PostFormDto postForm,
            BindingResult result,
            ModelMap map) {

        postService.create(userSess, postForm);
        return "redirect:/posts";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/edit")
    public String editHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @ModelAttribute PostFormDto post,
            @PathVariable Optional<Integer> id,
            BindingResult result,
            ModelMap map) {

        postService.update(userSess, post, id);
        return "redirect:/posts";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/delete")
    public String deleteHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable Optional<Integer> id) {

        postService.delete(userSess, id);
        return "redirect:/posts";
    }

}
