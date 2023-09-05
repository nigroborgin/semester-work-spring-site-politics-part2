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
import ru.kpfu.itis.shkalin.spring_site_politics.util.ControllerUtil;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/selections-books")
public class SelectionBookController {

    @Autowired
    private SelectionBookService selectionBookService;

//..................................................ОБЩИЕ ПОДБОРКИ КНИГ.................................................
    @GetMapping
    public String showAll(
            @AuthenticationPrincipal CustomUserDetails userSess,
            ModelMap modelMap) {

        selectionBookService.showAll(modelMap);
        ControllerUtil.enableButtonsIfAdmin(userSess, modelMap);

        return "selections/book-selections-list";
    }

    @GetMapping("/{id}")
    public String showOne(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable(required = false) Optional<Integer> id,
            ModelMap modelMap) {

        selectionBookService.showOne(id, modelMap);
        ControllerUtil.enableButtonsIfAdmin(userSess, modelMap);

        return "selections/book-selection";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String add(
            @AuthenticationPrincipal CustomUserDetails userSess,
            ModelMap modelMap) {

        selectionBookService.showNewForm(modelMap);
        ControllerUtil.disableButtons(modelMap);

        return "selections/book-selections-form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String edit(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable(required = false) Optional<Integer> id,
            ModelMap modelMap) {

        selectionBookService.showEditForm(id, modelMap);
        ControllerUtil.enableButtonsIfAdmin(userSess, modelMap);

        return "selections/book-selections-form";
    }

    @GetMapping("/{id}/download")
    public String downloadArchive(
            @PathVariable Optional<Integer> id) {

        String archiveName = selectionBookService.download(id);

        return "redirect:/file/archives/" + archiveName;
    }

//.........................................................POST.........................................................
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public String addHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @Valid @ModelAttribute("selectionBookInfo") SelectionBookFormDto selectionBookFormDto,
            BindingResult bindingResult,
            ModelMap modelMap) {

        boolean isValidDefault = ControllerUtil.validateDefault(modelMap, bindingResult);

        if (isValidDefault) {
            selectionBookService.create(userSess, selectionBookFormDto);
            return "redirect:/selections-books";
        } else {
            ControllerUtil.disableButtons(modelMap);
            selectionBookService.showNewFormWithNewData(selectionBookFormDto, modelMap);
            return showForm();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/edit")
    public String editHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @Valid @ModelAttribute("selectionBookInfo") SelectionBookFormDto selectionBookFormDto,
            BindingResult bindingResult,
            ModelMap modelMap,
            @PathVariable Optional<Integer> id) {

        boolean isValidDefault = ControllerUtil.validateDefault(modelMap, bindingResult);

        if (isValidDefault) {
            selectionBookService.update(userSess, selectionBookFormDto, id);
            return "redirect:/selections-books";
        } else {
            ControllerUtil.enableButtonsIfAdmin(userSess, modelMap);
            selectionBookService.showEditFormWithNewData(id, selectionBookFormDto, modelMap);
            return showForm();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String deleteHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable Optional<Integer> id) {

        selectionBookService.delete(userSess, id);
        return "redirect:/selections-books";
    }


//.................................................ЛИЧНЫЕ ПОДБОРКИ КНИГ.................................................
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my")
    public String showAllMy(
            @AuthenticationPrincipal CustomUserDetails userSess,
            ModelMap modelMap) {

        selectionBookService.showAllMy(userSess, modelMap);
        modelMap.addAttribute("isMy", true);
        ControllerUtil.enableButtons(modelMap);

        return "selections/book-selections-list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my/{id}")
    public String showOneMy(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable(required = false) Optional<Integer> id,
            ModelMap modelMap) {

        selectionBookService.showOneMy(userSess, id, modelMap);
        modelMap.addAttribute("isMy", true);
        ControllerUtil.enableButtons(modelMap);

        return "selections/book-selection";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my/new")
    public String addMy(
            ModelMap modelMap) {

        selectionBookService.showNewFormMy(modelMap);
        modelMap.addAttribute("isMy", true);
        ControllerUtil.disableButtons(modelMap);

        return "selections/book-selections-form";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my/{id}/edit")
    public String editMy(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable Optional<Integer> id,
            ModelMap modelMap) {

        selectionBookService.showEditFormMy(userSess, id, modelMap);
        modelMap.addAttribute("isMy", true);
        ControllerUtil.enableButtons(modelMap);

        return "selections/book-selections-form";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my/{id}/download")
    public String downloadArchiveMy(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable Optional<Integer> id) {

        String archiveName = selectionBookService.downloadMy(userSess, id);
        return "redirect:/file/archives/" + archiveName;
    }

//.........................................................POST.........................................................
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/my/new")
    public String addMyHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @Valid @ModelAttribute("selectionBookInfo") SelectionBookFormDto selectionBookFormDto,
            BindingResult bindingResult,
            ModelMap modelMap) {

        boolean isValidDefault = ControllerUtil.validateDefault(modelMap, bindingResult);

        if (isValidDefault) {
            selectionBookService.createMy(userSess, selectionBookFormDto);
            return "redirect:/selections-books/my";
        } else {
            selectionBookService.showNewFormWithNewData(selectionBookFormDto, modelMap);
            modelMap.addAttribute("isMy", true);
            ControllerUtil.disableButtons(modelMap);
            return showForm();
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/my/{id}/edit")
    public String editMyHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @Valid @ModelAttribute("selectionBookInfo") SelectionBookFormDto selectionBookFormDto,
            BindingResult bindingResult,
            ModelMap modelMap,
            @PathVariable Optional<Integer> id) {

        boolean isValidDefault = ControllerUtil.validateDefault(modelMap, bindingResult);

        if (isValidDefault) {
            selectionBookService.updateMy(userSess, selectionBookFormDto, id);
            return "redirect:/selections-books/my";
        } else {
            selectionBookService.showEditFormWithNewData(id, selectionBookFormDto, modelMap);
            modelMap.addAttribute("isMy", true);
            ControllerUtil.enableButtons(modelMap);
            return showForm();
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/my/{id}/delete")
    public String deleteMyHandle(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable Optional<Integer> id) {

        selectionBookService.deleteMy(userSess, id);
        return "redirect:/selections-books/my";
    }

    private String showForm() {
        return "/selections/book-selections-form";
    }

}
