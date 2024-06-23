package com.aboutdk.snowflakeid.service.impl;

import com.aboutdk.snowflakeid.POJO.DO.IdDO;
import com.aboutdk.snowflakeid.mybatisMapper.IdMapper;
import com.aboutdk.snowflakeid.service.ISnowflakeIDService;
import com.aboutdk.snowflakeid.util.Snowflake;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

@Service
public class SnowflakeIDServiceImpl implements ISnowflakeIDService {

   @Autowired
   private IdMapper idMapper;

   @Autowired
   private Snowflake snowflake;


   @Override
   public int addID(long id, String creator, String action) {
      long[] parse = snowflake.parse(id);

      QueryWrapper<IdDO> queryWrapper = new QueryWrapper<>();
      int count =  idMapper.selectCount(queryWrapper).intValue();

      IdDO idDO = IdDO.builder()
              .id(id)
              .parseStr(Arrays.toString(parse))
              .createDateTime(new Date().toString())
              .creator(creator)
              .action(action)
              .i(count + 1)
              .build();

      return idMapper.insert(idDO);
   }
}
