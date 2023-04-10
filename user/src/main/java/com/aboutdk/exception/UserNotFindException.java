package com.aboutdk.exception;

/**
 * @author zhouyang
 */
public class UserNotFindException extends RuntimeException{
   public UserNotFindException(String message) {
      super(message);
   }

   public UserNotFindException() {

   }
}
