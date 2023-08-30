package ru.kpfu.itis.shkalin.spring_site_politics.util;

import org.springframework.ui.ModelMap;
import ru.kpfu.itis.shkalin.spring_site_politics.model.User;
import ru.kpfu.itis.shkalin.spring_site_politics.security.CustomUserDetails;

import java.util.Objects;

public class EnableButtonsUtil {

    public static void enable(ModelMap map) {
        map.put("showNew", true);
        map.put("showEdit", true);
        map.put("showDelete", true);
    }

    public static void enableIfAuthorOfContent(CustomUserDetails userSess, ModelMap map, User user) {
        boolean showNew = false;
        boolean showEdit = false;
        boolean showDelete = false;

        if (userSess != null) {
            User userFromSession = userSess.getUser();
            boolean isAccess = Objects.equals(user.getId(), userFromSession.getId())
                    || Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

            if (isAccess) {
                showNew = true;
                showEdit = true;
                showDelete = true;
            }
        }

        map.put("showNew", showNew);
        map.put("showEdit", showEdit);
        map.put("showDelete", showDelete);
    }

    public static void enableIfAdmin(CustomUserDetails userSess, ModelMap map) {
        boolean showNew = false;
        boolean showEdit = false;
        boolean showDelete = false;

        if (userSess != null) {
            boolean isAccess = Objects.equals(userSess.getUser().getRole().getName(), "ROLE_ADMIN");

            if (isAccess) {
                showNew = true;
                showEdit = true;
                showDelete = true;
            }
        }

        map.put("showNew", showNew);
        map.put("showEdit", showEdit);
        map.put("showDelete", showDelete);
    }

}
