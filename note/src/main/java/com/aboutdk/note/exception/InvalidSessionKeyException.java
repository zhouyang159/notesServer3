package com.aboutdk.note.exception;

public class InvalidSessionKeyException extends RuntimeException{
  public InvalidSessionKeyException(String message) {
    super(message);
  }

  public InvalidSessionKeyException() {

  }
}
