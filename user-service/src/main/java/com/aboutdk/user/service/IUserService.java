package com.aboutdk.user.service;


import com.aboutdk.user.POJO.DO.UserDO;
import com.aboutdk.user.POJO.FORM.RegisterUserForm;

public interface IUserService {

  UserDO register(RegisterUserForm form);
}
