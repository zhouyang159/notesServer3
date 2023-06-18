package com.aboutdk.note.service.impl;

import com.aboutdk.note.POJO.DO.UserDO;
import com.aboutdk.note.exception.UserLoginException;
import com.aboutdk.note.exception.UserNotFindException;
import com.aboutdk.note.mapper.mybatisMapper.UserMapper;
import com.aboutdk.note.POJO.FORM.ChangePasswordForm;
import com.aboutdk.note.POJO.FORM.LoginForm;
import com.aboutdk.note.POJO.FORM.RegisterUserForm;
import com.aboutdk.note.security.TokenService;
import com.aboutdk.note.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.aboutdk.note.utils.Utils;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

   @Autowired
   private UserMapper userMapper;

   @Autowired
   private TokenService tokenService;

   @Override
   public List<UserDO> list() {
      List<UserDO> userDOList = userMapper.selectList(null)
              .stream()
              .peek((userDO) -> userDO.setPassword(null))
              .collect(Collectors.toList());

      return userDOList;
   }

   @Override
   public UserDO register(RegisterUserForm form) {
      QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("username", form.getUsername());
      UserDO oldUserDO = userMapper.selectOne(queryWrapper);
      if (oldUserDO != null) {
         throw new RuntimeException("此用户名已经被注册");
      }

      UserDO userDO = new UserDO();
      BeanUtils.copyProperties(form, userDO);

      int row = userMapper.insert(userDO);
      if (row < 1) {
         throw new RuntimeException("插入新注册用户失败");
      }

      return userDO;
   }

   @Override
   public String login(String username, String md5password) {
      UserDO userDO = this.findUserDO(username);
      if (userDO == null) {
         throw new UserNotFindException();
      }

      String hashPassword = Utils.MD5(userDO.getPassword());
      if (!md5password.equals(hashPassword)) {
         log.warn("client password: {}", md5password);
         log.warn("mysql password: {}", hashPassword);
         throw new UserLoginException();
      }

      // verify pass
      String token = tokenService.generateToken(username);
      return token;
   }

   @Override
   public UserDO changePassword(ChangePasswordForm form) {
      UserDO find = this.findUserDO(form.getUsername());

      if (find.getUsername().equals(form.getUsername()) && find.getPassword().equals(form.getOldPassword())) {
         // verify pass
         find.setPassword(form.getNewPassword());
         int row = userMapper.updateById(find);
         if (row < 1) {
            throw new RuntimeException("something wrong when update user");
         }
      } else {
         throw new UserLoginException();
      }

      return find;
   }

   @Override
   public UserDO updateProfile(UserDO userDO) {

      userDO.setUsername(null);
      userDO.setNotePassword(null);
      userDO.setPassword(null);
      userMapper.updateById(userDO);

      return userMapper.selectById(userDO.getId());
   }

   @Override
   public void delete(LoginForm form) {
      UserDO find = this.findUserDO(form.getUsername());

      if (!find.getUsername().equals(form.getUsername()) || !find.getPassword().equals(form.getPassword())) {
         throw new UserLoginException();
      }

      int row = userMapper.deleteById(find);
      if (row < 1) {
         throw new RuntimeException("delete user wrong");
      }
   }

   @Override
   public UserDO findUserDO(String username) {
      QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("username", username);
      UserDO userDO = userMapper.selectOne(queryWrapper);

      return userDO;
   }

   @Override
   public UserDO findUserDOByOpenid(String openid) {
      QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("openid", openid);
      UserDO userDO = userMapper.selectOne(queryWrapper);

      return userDO;
   }
}
