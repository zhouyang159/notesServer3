package com.aboutdk.user.service.impl;


import com.aboutdk.user.POJO.DO.UserDO;
import com.aboutdk.user.POJO.FORM.RegisterUserForm;
import com.aboutdk.user.mybatisMapper.UserMapper;
import com.aboutdk.user.service.IUserService;
import com.aboutdk.user.util.Snowflake;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserServiceImpl implements IUserService {

   @Autowired
   private UserMapper userMapper;

   @Autowired
   private Snowflake snowflake;

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

      long id = snowflake.nextId();
      userDO.setId(id);

      int row = userMapper.insert(userDO);
      if (row < 1) {
         throw new RuntimeException("插入新注册用户失败");
      }

      return userDO;
   }
}
