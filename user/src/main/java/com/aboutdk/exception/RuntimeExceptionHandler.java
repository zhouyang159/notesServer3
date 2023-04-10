package com.aboutdk.exception;

import com.aboutdk.POJO.VO.ResponseVO;
import com.aboutdk.enums.ResponseEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

/**
 * @author zhouyang
 */
@ControllerAdvice
public class RuntimeExceptionHandler {

   @ExceptionHandler(RuntimeException.class)
   @ResponseBody
   public ResponseEntity<ResponseVO> runtimeExceptionHandler(RuntimeException e) {
      String msg = "";
      if (e.getMessage() != null) {
         msg = e.getMessage();
      } else {
         msg = ResponseEnum.ERROR.getDesc();
      }

      ResponseVO data = ResponseVO.error(ResponseEnum.ERROR, msg);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
   }

   @ExceptionHandler(UserLoginException.class)
   @ResponseBody
   public ResponseEntity<ResponseVO> userLoginExceptionHandler(UserLoginException e) {
      ResponseVO data = ResponseVO.error(ResponseEnum.USERNAME_OR_PASSWORD_INCORRECT);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(data);
   }

   @ExceptionHandler(InvalidTokenException.class)
   @ResponseBody
   public ResponseEntity<ResponseVO> invalidTokenExceptionHandler(InvalidTokenException e) {
      String message = e.getMessage();
      ResponseVO data = ResponseVO.error(ResponseEnum.TOKEN_INVALID, message);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(data);
   }

   @ExceptionHandler(InvalidSessionKeyException.class)
   @ResponseBody
   public ResponseEntity<ResponseVO> invalidSessionKeyExceptionHandler(InvalidSessionKeyException e) {
      String message = e.getMessage();
      ResponseVO data = ResponseVO.error(ResponseEnum.TOKEN_INVALID, message);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(data);
   }

   @ExceptionHandler(MethodArgumentNotValidException.class)
   @ResponseBody
   public ResponseEntity<ResponseVO> notValidExceptionHandler(MethodArgumentNotValidException e) {
      BindingResult bindingResult = e.getBindingResult();

      Objects.requireNonNull(bindingResult.getFieldError());
      ResponseVO data = ResponseVO.error(ResponseEnum.PARAM_ERROR,
              bindingResult.getFieldError().getField() + " " + bindingResult.getFieldError().getDefaultMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(data);
   }

   @ExceptionHandler(UserNotFindException.class)
   @ResponseBody
   public ResponseEntity<ResponseVO> notFindExceptionHandler(UserNotFindException e) {
      ResponseVO data = ResponseVO.error(ResponseEnum.USERNAME_NOT_EXIST);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(data);
   }

   @ExceptionHandler(UpdateNoteVersionWrongException.class)
   @ResponseBody
   public ResponseEntity<ResponseVO> UpdateNoteVersionWrongExceptionHandler(UpdateNoteVersionWrongException e) {
      ResponseVO data = ResponseVO.error(ResponseEnum.UPDATE_NOTE_VERSION_WRONG, e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(data);
   }
}
