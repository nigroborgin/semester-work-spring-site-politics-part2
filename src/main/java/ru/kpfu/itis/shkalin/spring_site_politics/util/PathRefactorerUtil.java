package ru.kpfu.itis.shkalin.spring_site_politics.util;

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

}
