package com.aboutdk.user.service.impl;


import com.aboutdk.user.POJO.DO.UserDO;
import com.aboutdk.user.POJO.FORM.RegisterUserForm;
import com.aboutdk.user.mybatisMapper.UserMapper;
import com.aboutdk.user.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
public class UserServiceImpl implements IUserService {

   @Autowired
   private UserMapper userMapper;

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

      RestTemplate restTemplate = new RestTemplate();
      String snowflakeID = restTemplate.getForObject("http://localhost:8082/snowflake/id", String.class);

      long id = Long.valueOf(snowflakeID);
      userDO.setId(id);

      int row = userMapper.insert(userDO);
      if (row < 1) {
         throw new RuntimeException("插入新注册用户失败");
      }

      return userDO;
   }
}
