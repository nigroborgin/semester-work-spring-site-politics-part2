package ru.kpfu.itis.shkalin.spring_site_politics.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exc) throws IOException, ServletException {

//        log(request, exc);
//
//        request.getHttpServletMapping();
////        request.setAttribute("message", "You do not have the rights for open this page.");
//        request.setAttribute("url", request.getRequestURL());
//
//        request.getRequestDispatcher(request.getContextPath() + "/errors/403_access_denied").forward(request, response);
//
////        response.sendRedirect(/*request.getContextPath() + */"/errors/403_access_denied");
    }

    private void log(HttpServletRequest req, Exception e) {
        LOGGER.warn("URL: " + req.getRequestURL() + "; Message: " + e.getMessage());
    }

}