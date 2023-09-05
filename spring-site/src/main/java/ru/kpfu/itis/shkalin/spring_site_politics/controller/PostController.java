package ru.kpfu.itis.shkalin.spring_site_politics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.post.PostFormDto;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.post.PostViewDto;
import ru.kpfu.itis.shkalin.spring_site_politics.security.CustomUserDetails;
import ru.kpfu.itis.shkalin.spring_site_politics.service.db.PostService;
import ru.kpfu.itis.shkalin.spring_site_politics.util.ControllerUtil;
import ru.kpfu.itis.shkalin.spring_site_politics.util.ConverterUtil;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public String showAll(
            @AuthenticationPrincipal CustomUserDetails userSess,
            ModelMap modelMap) {

        ControllerUtil.enableButtonsIfAdmin(userSess, modelMap);
        ControllerUtil.enableButtonNewIfAuth(userSess, modelMap);
        postService.showAll(modelMap);

        return "/posts/post-list";
    }

    @GetMapping("/{id}")
    public String showOne(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable(required = false) Optional<Integer> id,
            ModelMap modelMap) {

        ControllerUtil.enableButtonsIfAdminOrAuthor(userSess, postService.getPostById(id).getUser(), modelMap);
        postService.showOne(id, modelMap);

        return "/posts/post";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/new")
    public String add(
            @AuthenticationPrincipal CustomUserDetails userSess,
            ModelMap modelMap) {

        ControllerUtil.disableButtons(modelMap);
        postService.showNewForm(userSess, modelMap);

        return "/posts/post-form";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/edit")
    public String edit(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable(required = false) Optional<Integer> id,
            ModelMap modelMap) {

        ControllerUtil.enableButtonsIfAdminOrAuthor(userSess, postService.getPostById(id).getUser(), modelMap);
        postService.showEditForm(id, modelMap);

        return "/posts/post-form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/new")
    public String addHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @Valid @ModelAttribute("postForm") PostFormDto postFormDto,
            BindingResult bindingResult,
            ModelMap modelMap) {

        boolean isValidDefault = ControllerUtil.validateDefault(modelMap, bindingResult);

        if (isValidDefault) {
            postService.create(userSess, postFormDto);
            return "redirect:/posts";
        } else {
            ControllerUtil.disableButtons(modelMap);
            postService.showNewFormWithNewData(userSess, postFormDto, modelMap);
            return showForm();
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/edit")
    public String editHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @Valid @ModelAttribute("postForm") PostFormDto postFormDto,
            BindingResult bindingResult,
            @PathVariable Optional<Integer> id,
            ModelMap modelMap) {

        boolean isValidDefault = ControllerUtil.validateDefault(modelMap, bindingResult);

        if (isValidDefault) {
            postService.update(userSess, postFormDto, id);
            return "redirect:/posts";
        } else {
            ControllerUtil.enableButtonsIfAdminOrAuthor(userSess, postService.getPostById(id).getUser(), modelMap);
            postService.showEditFormWithNewData(id, postFormDto, modelMap);

            return showForm();
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/delete")
    public String deleteHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable Optional<Integer> id) {

        postService.delete(userSess, id);
        return "redirect:/posts";
    }

    private String showForm() {
        return "/posts/post-form";
    }

}
