package ru.kpfu.itis.shkalin.spring_site_politics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CustomAccessDeniedException extends RuntimeException {

    public CustomAccessDeniedException() {
        super();
    }

    public CustomAccessDeniedException(String message) {
        super(message);
    }
}
