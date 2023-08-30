package ru.kpfu.itis.shkalin.spring_site_politics.controller;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.book.BookFormDto;
import ru.kpfu.itis.shkalin.spring_site_politics.security.CustomUserDetails;

import java.io.IOException;
import java.util.Optional;

public interface CrudController {

    String showAll(
            CustomUserDetails userSess,
            ModelMap map);

    String showOne(
            CustomUserDetails userSess,
            Optional<Integer> id,
            ModelMap map);

    String add(
            CustomUserDetails userSess,
            ModelMap map);

    String edit(
            CustomUserDetails userSess,
            Optional<Integer> id,
            ModelMap map);

    String addHandle(
            CustomUserDetails userSess,
            BookFormDto bookFormDto,
            MultipartFile bookFile,
            BindingResult result,
            ModelMap map) throws IOException;

    String editHandle(
            CustomUserDetails userSess,
            BookFormDto bookFormDto,
            MultipartFile bookFile,
            Optional<Integer> id) throws IOException;

    String deleteHandle(
            CustomUserDetails userSess,
            Optional<Integer> id);
}
