package com.aboutdk.note.interceptor;

import com.aboutdk.note.exception.InvalidTokenException;
import com.aboutdk.note.security.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {

  private TokenService tokenService;

  public UserLoginInterceptor(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    // Todo youHua
    String token = request.getHeader("token");
    if (token == null || "null".equals(token) || StringUtils.isEmpty(token)) {
      throw new InvalidTokenException("token miss");
    }
      // websocket sessionKey
//    String sessionKey = request.getHeader("sessionKey");
//    if (sessionKey == null || sessionKey.equals("null")) {
//      throw new InvalidSessionKeyException("sessionKey miss");
//    }

    try {
      tokenService.validateToken(token);
    }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.warn("Invalid JWT signature trace: {}", e.getMessage());
      throw new InvalidTokenException("Invalid JWT signature.");
    } catch (ExpiredJwtException e) {
      log.warn("Expired JWT token trace: {}", e.getMessage());
      throw new InvalidTokenException("Expired JWT token.");
    } catch (UnsupportedJwtException e) {
      log.warn("Unsupported JWT token trace: {}", e.getMessage());
      throw new InvalidTokenException("Unsupported JWT token.");
    } catch (IllegalArgumentException e) {
      log.warn("JWT token compact of handler are invalid trace: {}", e.getMessage());
      throw new InvalidTokenException("JWT token compact of handler are invalid.");
    } catch (Exception e) {
      log.error("token invalid: {}", e.getMessage());
      throw new InvalidTokenException("token invalid");
    }

    return true;
  }
}
