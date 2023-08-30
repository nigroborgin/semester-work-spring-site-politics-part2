package ru.kpfu.itis.shkalin.spring_site_politics.service.file_storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.shkalin.spring_site_politics.exception.StorageNotFoundException;
import ru.kpfu.itis.shkalin.spring_site_politics.util.PathRefactorerUtil;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileSystemStorageService implements StorageService {

    @Value("${upload.realpath}")
    private String uploadPath;

//    private final Path rootLocation;

    public FileSystemStorageService() {
    }

    @Override
    public String saveResource(MultipartFile file, String resourceType, String filename) {
        try {

            // check
            if (file.isEmpty()) {
                throw new StorageNotFoundException("file", "Failed to store empty file. File is empty!");
            }
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            // get valid filepath
            String fileExtension = PathRefactorerUtil.getExtension(file);
            String newFileName = filename + '_' + UUID.randomUUID() + "." + fileExtension;
            String fullUploadPath = PathRefactorerUtil.concatAndAddSeparator(uploadPath, resourceType, newFileName);

            // write data
            byte[] bytes = file.getBytes();
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(fullUploadPath)))) {
                stream.write(bytes);
                stream.flush();
            }

            return newFileName;

        } catch (IOException e) {
            throw new StorageNotFoundException("file", "Failed to store file.", e);
        }
    }

    @Override
    public Resource getResourceByName(String resourceType, String filename) {
        try {
            Resource resource = new UrlResource(
                    Path.of(PathRefactorerUtil.concatAndAddSeparator(uploadPath, resourceType, filename))
                            .toUri()
            );
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageNotFoundException("file", "Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageNotFoundException("file", "Could not read file: " + filename, e);
        }
    }

    public String createArchive(List<String> filesPaths) throws IOException {

        String archiveName = "archive_" + UUID.randomUUID() + ".zip";
        String fullArchiveName = uploadPath + File.separator + "archives" + File.separator + archiveName;

        final FileOutputStream fos = new FileOutputStream(fullArchiveName);
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for (String srcFile : filesPaths) {
            Resource book = getResourceByName("books", srcFile);
            InputStream inputStream = book.getInputStream();
            ZipEntry zipEntry = new ZipEntry(book.getFilename());
            zipOut.putNextEntry(zipEntry);

            byte[] bytesBuffer = new byte[1024];
            int length;
            while ((length = inputStream.read(bytesBuffer)) >= 0) {
                zipOut.write(bytesBuffer, 0, length);
            }
            inputStream.close();
        }
        zipOut.close();
        fos.close();

        return archiveName;
    }

}