package ru.kpfu.itis.shkalin.spring_site_politics.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

public class FileUploaderUtil {

    public static String uploadFile(MultipartFile picture, String uploadPath) throws IOException {
        return uploadFile(picture, uploadPath, picture.getName());
    }

    public static String uploadFile(MultipartFile file, String uploadPath, String fileName) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IOException("File is null or empty!");
        }

        String fileExtension = getExtension(file);
        String newFileName = fileName + '_' + UUID.randomUUID() + "." + fileExtension;
        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String newUploadPath = uploadPath + File.separator + newFileName;
//            file.transferTo(new File(newUploadPath));

        byte[] bytes = file.getBytes();
        File file1 = new File(newUploadPath);
        FileOutputStream fileOutputStream = new FileOutputStream(file1);
        BufferedOutputStream stream = new BufferedOutputStream(fileOutputStream);
        stream.write(bytes);
        stream.flush();
        stream.close();

        return newFileName;

    }

    private static String getExtension(MultipartFile file) {
        String[] fileNameSplit = file.getOriginalFilename().split("\\.");
        int lengthFilePathSplitArray = fileNameSplit.length;
        return fileNameSplit[lengthFilePathSplitArray - 1];
    }
}
