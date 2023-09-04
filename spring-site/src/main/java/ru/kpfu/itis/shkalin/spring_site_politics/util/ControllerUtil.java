package ru.kpfu.itis.shkalin.spring_site_politics.util;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
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

}
