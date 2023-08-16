package ru.kpfu.itis.shkalin.spring_site_politics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class StorageNotFoundException extends NotFoundException {

    public StorageNotFoundException() {
        super();
    }

    public StorageNotFoundException(String entity) {
        super(entity);
    }

    public StorageNotFoundException(String entity, String message) {
        super(entity, message);
    }

    public StorageNotFoundException(String entity, String message, Throwable cause) {
        super(entity, message, cause);
    }

}
