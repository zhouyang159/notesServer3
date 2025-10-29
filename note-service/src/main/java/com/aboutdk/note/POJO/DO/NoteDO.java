package com.aboutdk.note.POJO.DO;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName(value = "note")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NoteDO {

   @TableId
   private String id;

   private String title;

   private String content;

   private Integer number;

   private String username;

   private Boolean encrypt = false;

   @TableField("create_time")
   private LocalDateTime createTime = LocalDateTime.now();

   @TableField("update_time")
   private LocalDateTime updateTime;

   @TableField("delete_time")
   private LocalDateTime deleteTime = null;

   private Integer deleted = 0;

   private Boolean deleteForever = false;

   private Integer version = 0;
}