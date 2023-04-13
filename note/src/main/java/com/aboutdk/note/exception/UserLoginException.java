package com.aboutdk.note.exception;

public class UserLoginException extends RuntimeException {
   public UserLoginException(String message) {
      super(message);
   }

   public UserLoginException() {

   }
}
