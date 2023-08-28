package ru.kpfu.itis.shkalin.spring_site_politics.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class PathRefactorerUtil {

    public static String changeSeparator(String path, String separatorBefore, String separatorAfter) {
        String[] splittingPath = path.split(separatorBefore);
        StringBuilder newPath = new StringBuilder();
        newPath.append(separatorAfter);

        for (String part : splittingPath) {
            newPath.append(part).append(separatorAfter);
        }

        return newPath.toString();
    }

    public static String concatAndAddSeparator(String ... elements) {
        return concatAndAddCustomSeparator(File.separator, elements);
    }

    public static String concatAndAddCustomSeparator(String separator, String ... elements) {

        StringBuilder concat = new StringBuilder();
        for (int i = 0; i < elements.length-1; i++) {
            concat.append(elements[i]);
            concat.append(separator);
        }
        concat.append(elements[elements.length-1]);

        return concat.toString();
    }

    public static String getFileName(String separator, String pathToFile) {
        String[] splitUrl = pathToFile.split(separator);
        return splitUrl[splitUrl.length - 1];
    }

    public static String getExtension(MultipartFile file) {
        return getExtension(file.getOriginalFilename());
    }

    public static String getExtension(String filename) {
        String[] fileNameSplit = filename.split("\\.");
        int lengthFilePathSplitArray = fileNameSplit.length;
        return fileNameSplit[lengthFilePathSplitArray - 1];
    }

}
