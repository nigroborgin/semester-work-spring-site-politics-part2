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

    public static String concatAndAddCustomSeparator(String separator, String ... elem) {

        StringBuilder concat = new StringBuilder();
        for (int i = 0; i < elem.length-1; i++) {
            concat.append(elem[i]);
            concat.append(separator);
        }
        concat.append(elem[elem.length-1]);

        return concat.toString();
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
