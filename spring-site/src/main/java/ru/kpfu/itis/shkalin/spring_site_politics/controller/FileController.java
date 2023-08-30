package ru.kpfu.itis.shkalin.spring_site_politics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.shkalin.spring_site_politics.service.file_storage.StorageService;

import java.util.Optional;


@Controller
@RequestMapping("/file")
public class FileController {

    private final StorageService storageService;

    @Autowired
    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/{resourceType}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(
            @PathVariable Optional<String> resourceType,
            @PathVariable String filename) {

        Resource file = storageService.getResourceByName(resourceType.orElse(""), filename);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

}
