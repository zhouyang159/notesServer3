package com.aboutdk.service;


import com.aboutdk.POJO.DO.UserDO;
import com.aboutdk.POJO.FORM.RegisterUserForm;

import java.util.List;

public interface IUserService {

  UserDO register(RegisterUserForm form);
}
