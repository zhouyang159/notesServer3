package com.aboutdk.snowflakeid.service.impl;

import com.aboutdk.snowflakeid.POJO.DO.IdDO;
import com.aboutdk.snowflakeid.mybatisMapper.IdMapper;
import com.aboutdk.snowflakeid.service.ISnowflakeIDService;
import com.aboutdk.snowflakeid.util.Snowflake;
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

      IdDO idDO = IdDO.builder()
              .id(id)
              .parseStr(Arrays.toString(parse))
              .createDateTime(new Date().toString())
              .creator(creator)
              .action(action)
              .build();

      return idMapper.insert(idDO);
   }
}
