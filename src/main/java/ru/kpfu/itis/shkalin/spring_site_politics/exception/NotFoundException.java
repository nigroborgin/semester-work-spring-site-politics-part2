package ru.kpfu.itis.shkalin.spring_site_politics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{
    /**
     * Default entity that has not been found. Could be page, account, product etc
     */
    protected String entity = "page";

    public NotFoundException() {
        super();
    }

    public NotFoundException(String entity) {
        super();
        this.entity = entity;
    }

    public NotFoundException(String entity, String message) {
        super(message);
        this.entity = entity;
    }

    public NotFoundException(String entity, String message, Throwable cause) {
        super(message, cause);
        this.entity = entity;
    }

    public String getEntity(){
        return entity;
    }
}
