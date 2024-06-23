package com.aboutdk.note.enums;

public enum ResponseEnum {

  SUCCESS(0, "success"),

  ERROR(-1, "server error"),

  USERNAME_EXIST(1, "username exist"),

  USERNAME_OR_PASSWORD_INCORRECT(2, "username or password incorrect"),

  USERNAME_NOT_EXIST(3, "username not exist"),

  NOTE_ID_NOT_EXIST(4, "note id not exist"),

  PARAM_ERROR(5, "param error"),

  NEED_LOGIN(6, "need login"),

  TOKEN_INVALID(7, "Token invalid"),

  TOKEN_MISS(8, "token miss"),

  NOTE_NOT_FIND(9, "note not find"),

  UPDATE_NOTE_VERSION_WRONG(10, "note version wrong");

  Integer code;

  String desc;

  ResponseEnum(Integer code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public Integer getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }
}
