package ru.kpfu.itis.shkalin.spring_site_politics.controller;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.trivee.fb2pdf.FB2toPDFException;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.book.BookFormDto;
import ru.kpfu.itis.shkalin.spring_site_politics.security.CustomUserDetails;
import ru.kpfu.itis.shkalin.spring_site_politics.service.db.BookService;
import ru.kpfu.itis.shkalin.spring_site_politics.util.ControllerUtil;

import javax.validation.Valid;
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
            ModelMap modelMap) {

        ControllerUtil.enableButtonsIfAdmin(userSess, modelMap);
        bookService.showAll(modelMap);

        return "/books/book-list";
    }

    @GetMapping("/{id}")
    public String showOne(
            @AuthenticationPrincipal CustomUserDetails userSess,
            @PathVariable(required = false) Optional<Integer> id,
            ModelMap modelMap) {

        ControllerUtil.enableButtonsIfAdmin(userSess, modelMap);
        bookService.showOne(id, modelMap);

        return "/books/book";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String add(
            ModelMap modelMap) {

        ControllerUtil.disableButtons(modelMap);
        bookService.showNewForm(modelMap);

        return showForm();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String edit(
            @PathVariable(required = false) Optional<Integer> id,
            ModelMap modelMap) {

        ControllerUtil.enableButtons(modelMap);
        bookService.showEditForm(id, modelMap);

        return showForm();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public String addHandle(
            @Valid @ModelAttribute("bookInfo") BookFormDto bookFormDto,
            BindingResult bindingResult,
            @RequestParam("file") MultipartFile bookFile,
            ModelMap modelMap) throws IOException, DocumentException, FB2toPDFException {

        boolean isValidDefault = ControllerUtil.validateDefault(modelMap, bindingResult);

        if (isValidDefault) {
            bookService.create(bookFormDto, bookFile);
            return "redirect:/books";
        } else {
            ControllerUtil.disableButtons(modelMap);
            bookService.showNewFormWithNewData(bookFormDto, modelMap);
            return showForm();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/edit")
    public String editHandle(
            @Valid @ModelAttribute("bookInfo") BookFormDto bookFormDto,
            BindingResult bindingResult,
            @RequestParam("file") MultipartFile bookFile,
            @PathVariable Optional<Integer> id,
            ModelMap modelMap) throws IOException, DocumentException, FB2toPDFException {

        boolean isValidDefault = ControllerUtil.validateDefault(modelMap, bindingResult);

        if (isValidDefault) {
            bookService.update(bookFormDto, bookFile, id);
            return "redirect:/books";
        } else {
            ControllerUtil.enableButtons(modelMap);
            bookService.showEditFormWithNewData(id, bookFormDto, modelMap);

            return showForm();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String deleteHandle(
            @PathVariable Optional<Integer> id) {

        bookService.delete(id);
        return "redirect:/books";
    }

    private String showForm() {
        return "/books/book-form";
    }

}
