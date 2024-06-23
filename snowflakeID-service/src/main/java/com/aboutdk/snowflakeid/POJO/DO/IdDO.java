package com.aboutdk.snowflakeid.POJO.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName(value = "snowflake_id")
public class IdDO {

   private long id;

   private String parseStr;

   private String createDateTime;

   private String creator;

   private String action;

   private Integer i;
}
