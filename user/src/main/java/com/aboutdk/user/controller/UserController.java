package com.aboutdk.user.controller;


import com.aboutdk.user.POJO.DO.UserDO;
import com.aboutdk.user.POJO.FORM.RegisterUserForm;
import com.aboutdk.user.POJO.VO.ResponseVO;
import com.aboutdk.user.mybatisMapper.UserMapper;
import com.aboutdk.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zhouyang
 */
@RestController
@Slf4j
public class UserController {

   @Autowired
   private IUserService userService;

   @Autowired
   private UserMapper userMapper;

   @PostMapping("/user")
   private ResponseVO<UserDO> register(@Valid @RequestBody RegisterUserForm form, BindingResult bindingResult) {
      if (bindingResult.hasErrors()) {
         List<FieldError> fieldErrors = bindingResult.getFieldErrors();
         log.error("error field is : {} ,message is : {}", fieldErrors.get(0).getField(), fieldErrors.get(0).getDefaultMessage());
         throw new RuntimeException(fieldErrors.get(0).getField() + " " +fieldErrors.get(0).getDefaultMessage());
      }

      if (form.getUsername().trim().contains(" ")) {
         throw new RuntimeException("username must not contain space");
      }
      if (form.getPassword().trim().contains(" ")) {
         throw new RuntimeException("password must not contain space");
      }

      UserDO userDO = userService.register(form);
      userDO.setPassword(null);
      return ResponseVO.success(userDO);
   }
}
