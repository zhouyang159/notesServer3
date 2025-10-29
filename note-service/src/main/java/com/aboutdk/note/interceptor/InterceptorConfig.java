package com.aboutdk.note.interceptor;

import com.aboutdk.note.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

  @Autowired
  private TokenService tokenService;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {

    registry.addInterceptor(new ShowIPAddressInterceptor());

    registry.addInterceptor(new UserLoginInterceptor(tokenService))
            .addPathPatterns("/**")
            .excludePathPatterns(
                    "/api/user/login",
                    "/public/**"
            );
  }
}
