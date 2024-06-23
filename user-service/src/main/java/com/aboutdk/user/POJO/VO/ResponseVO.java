package com.aboutdk.user.POJO.VO;

import com.aboutdk.user.enums.ResponseEnum;
import lombok.Data;

@Data
public class ResponseVO<T> {

  private Integer status;

  private String msg;

  private T data;

  public ResponseVO() {

  }

  public ResponseVO(Integer status, String msg) {
    this.status = status;
    this.msg = msg;
  }

  public ResponseVO(Integer status, T data) {
    this.status = status;
    this.data = data;
  }

  public ResponseVO(Integer status, String msg, T data) {
    this.status = status;
    this.msg = msg;
    this.data = data;
  }

  public static <T> ResponseVO<T> success() {
    return new ResponseVO<T>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getDesc());
  }

  public static <T> ResponseVO<T> success(T data) {
    return new ResponseVO<T>(ResponseEnum.SUCCESS.getCode(), "success", data);
  }

  public static <T> ResponseVO<T> error(ResponseEnum error, String message) {
    return new ResponseVO<T>(error.getCode(), message);
  }

  public static <T> ResponseVO<T> error(ResponseEnum error) {
    return new ResponseVO<T>(error.getCode(), error.getDesc());
  }
}
