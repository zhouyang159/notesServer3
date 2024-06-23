package com.aboutdk.note.service;

import com.aboutdk.note.POJO.FORM.ChangePasswordForm;
import com.aboutdk.note.POJO.FORM.LoginForm;
import com.aboutdk.note.POJO.FORM.RegisterUserForm;
import com.aboutdk.note.POJO.DO.UserDO;

import java.util.List;

public interface IUserService {

  List<UserDO> list();

  UserDO register(RegisterUserForm form);

  String login(String username, String password) throws Exception;

  UserDO changePassword(ChangePasswordForm form);

  UserDO updateProfile(UserDO userDO);

  void delete(LoginForm form);

  UserDO findUserDO(String username);
}
