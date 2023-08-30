package ru.kpfu.itis.shkalin.spring_site_politics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.user.UserFormProfileDto;
import ru.kpfu.itis.shkalin.spring_site_politics.security.CustomUserDetails;
import ru.kpfu.itis.shkalin.spring_site_politics.service.db.PostService;
import ru.kpfu.itis.shkalin.spring_site_politics.service.db.UserService;

import java.io.IOException;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @GetMapping(value = {"/users"})
    public String showAll(ModelMap map) {

        userService.showAll(map);
        return "/users/user-list";
    }

    @GetMapping("/users/{id}")
    public String showOne(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable(required = false) Optional<Integer> id,
            ModelMap map) {

        userService.showOne(userSess, id, map);
        return "/users/user";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal CustomUserDetails user) {

        return "redirect:/users/" + user.getUser().getId();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/users/{id}/edit")
    public String edit(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable(required = false) Optional<Integer> id,
            ModelMap map) {

        userService.showEditForm(userSess, id, map);
        return "/users/user-form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/users/{id}/edit")
    public String editHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable(required = false) Optional<Integer> id,
            @ModelAttribute("userEdit") UserFormProfileDto userEdit,
            @RequestParam("picture") MultipartFile picture,
            BindingResult result,
            ModelMap map) throws IOException {

        userService.update(userSess, id, userEdit, picture);
        return "redirect:/users/" + id.get();
    }

    @GetMapping("/users/{id}/posts")
    public String showPostsByUser(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable(required = false) Optional<Integer> id,
            ModelMap map) {

        postService.showByUserId(userSess, id, map);
        return "/posts/post-list";
    }

}
