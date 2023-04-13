package com.aboutdk.user.POJO.FORM;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class RegisterUserForm {

  @NotEmpty
  @Size(min=6, max=15)
  String username;

  @NotEmpty
  String password;
}
