package ru.kpfu.itis.shkalin.spring_site_politics.util;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.kpfu.itis.shkalin.spring_site_politics.model.User;
import ru.kpfu.itis.shkalin.spring_site_politics.security.CustomUserDetails;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ControllerUtil {

    public static Map<String, String> getErrorsMap(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField() + "Error",
                        FieldError::getDefaultMessage)
                );
    }

    public static boolean validateDefault(ModelMap modelMap, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            modelMap.mergeAttributes(getErrorsMap(bindingResult));
            return false;
        }
        return true;
    }

    public static boolean checkAuth(CustomUserDetails userSess) {
        return userSess != null;
    }

    public static boolean checkAuthor(User userFromEntity, CustomUserDetails userSess) {
        if (userSess != null) {
            return Objects.equals(userSess.getUser().getId(), userFromEntity.getId());
        } else {
            return false;
        }
    }

    public static boolean checkAdmin(CustomUserDetails userSess) {
        if (userSess != null) {
            return Objects.equals(userSess.getUser().getRole().getName(), "ROLE_ADMIN");
        } else {
            return false;
        }
    }

    public static void enableButtons(ModelMap modelMap) {
        modelMap.put("showNew", true);
        modelMap.put("showEdit", true);
        modelMap.put("showDelete", true);
    }

    public static void enableButtonNewIfAuth(CustomUserDetails userSess, ModelMap modelMap) {
        if (checkAuth(userSess)) {
            modelMap.put("showNew", true);
        }
    }

    public static void disableButtons(ModelMap modelMap) {
        modelMap.put("showNew", false);
        modelMap.put("showEdit", false);
        modelMap.put("showDelete", false);
    }

    public static void enableButtonsIfAdmin(CustomUserDetails userSess, ModelMap modelMap) {
        if (ControllerUtil.checkAdmin(userSess)) {
            ControllerUtil.enableButtons(modelMap);
        } else {
            ControllerUtil.disableButtons(modelMap);
        }
    }

    public static void enableButtonsIfAdminOrAuthor(CustomUserDetails userSess, User userFromEntity, ModelMap modelMap) {
        if (ControllerUtil.checkAdmin(userSess) || ControllerUtil.checkAuthor(userFromEntity, userSess)) {
            ControllerUtil.enableButtons(modelMap);
        } else {
            ControllerUtil.disableButtons(modelMap);
        }
    }

}
