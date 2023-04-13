package com.aboutdk.note.POJO.FORM;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangePasswordForm {

  @NotNull
  private String username;

  @NotNull
  private String oldPassword;

  @NotNull
  private String newPassword;
}
