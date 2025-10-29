package com.aboutdk.note.log;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    private final HttpServletRequest request;

    public LoggingAspect(HttpServletRequest request) {
        this.request = request;
    }

    // Pointcut to match all methods in classes annotated with @RestController
    @Before("within(com.aboutdk.note.controller.NoteController)")
    public void logBeforeControllerMethods() {
        String requestUrl = request.getRequestURL().toString();
        log.info("A request is coming to the controller. URL: {}", requestUrl);
    }
}