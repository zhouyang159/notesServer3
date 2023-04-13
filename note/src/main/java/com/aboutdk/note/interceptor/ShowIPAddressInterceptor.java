package com.aboutdk.note.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ShowIPAddressInterceptor implements HandlerInterceptor {

   @Override
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
      String method = request.getMethod();
      StringBuffer requestUrl = request.getRequestURL();
      log.info("{} {}",  method, requestUrl );
      return true;
   }
}
