package com.aboutdk.note.service.impl;

import com.aboutdk.note.POJO.DO.IdDO;
import com.aboutdk.note.mapper.mybatisMapper.IdMapper;
import com.aboutdk.note.service.ISnowflakeIDService;
import com.aboutdk.note.util.Snowflake;
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
   public int addID(long id) {
      long[] parse = snowflake.parse(id);

      IdDO idDO = IdDO.builder()
              .id(id)
              .parseStr(Arrays.toString(parse))
              .createDateTime(new Date().toString())
              .build();

      return idMapper.insert(idDO);
   }
}
