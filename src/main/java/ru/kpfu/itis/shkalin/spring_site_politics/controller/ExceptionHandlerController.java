package ru.kpfu.itis.shkalin.spring_site_politics.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.kpfu.itis.shkalin.spring_site_politics.exception.CustomAccessDeniedException;
import ru.kpfu.itis.shkalin.spring_site_politics.exception.NotFoundException;
import ru.kpfu.itis.shkalin.spring_site_politics.exception.StorageNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerController /*extends ResponseEntityExceptionHandler*/ {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @Order(10)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            ServletRequestBindingException.class,
            TypeMismatchException.class,
            MethodArgumentNotValidException.class,
            BindException.class
    })
    public ModelAndView badRequest(HttpServletRequest req, Exception exception) {

        log(req, exception);
        return getModelAndView(null, req, "/errors/400_bad_request");
    }

    @Order(20)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({
            CustomAccessDeniedException.class,
    })
    public ModelAndView forbidden(HttpServletRequest req, Exception exception) {

        log(req, exception);
        return getModelAndView(exception.getMessage(), req, "/errors/403_access_denied");
    }

    @Order(30)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            NotFoundException.class,
            StorageNotFoundException.class
    })
    public ModelAndView notFound(HttpServletRequest req, Exception exception) {

        log(req, exception);
        ModelAndView mav = getModelAndView(exception.getMessage(), req, "/errors/404_not_found");
        String entity = ((NotFoundException) exception).getEntity();
        mav.addObject("entity", entity);
        return mav;
    }

    @Order(35)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handlerNotFound(HttpServletRequest req, Exception exception) {

        log(req, exception);
        return getModelAndView(null, req, "/errors/404_not_found");
    }

    @Order(40)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class
    })
    public ModelAndView methodNotAllowed(HttpServletRequest req, Exception exception) {

        log(req, exception);
        return getModelAndView("You are trying to do something wrong.", req, "/errors/405_method_not_allowed");
    }

    @Order(50)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            ConversionNotSupportedException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class,
            ServletException.class,
            Exception.class
    })
    public ModelAndView handleDefault(HttpServletRequest req, Exception exception) {

        log(req, exception);
        return getModelAndView(null, req, "/errors/500_default_error_page");
    }

    private void log(HttpServletRequest req, Exception e) {
        LOGGER.warn("URL: " + req.getRequestURL() + "; Message: " + e.getMessage(), e);
    }

    private ModelAndView getModelAndView(String message, HttpServletRequest req, String viewName) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", message);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(viewName);
        return mav;
    }

}
