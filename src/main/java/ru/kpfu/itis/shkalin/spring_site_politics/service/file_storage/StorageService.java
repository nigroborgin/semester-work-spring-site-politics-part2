package ru.kpfu.itis.shkalin.spring_site_politics.service.file_storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String saveResource(MultipartFile file, String resourceType, String filename);

    Resource getResourceByName(String resourceType, String filename);

}
