package com.aboutdk.exception;

public class UserLoginException extends RuntimeException {
   public UserLoginException(String message) {
      super(message);
   }

   public UserLoginException() {

   }
}
