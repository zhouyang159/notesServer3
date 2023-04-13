package com.aboutdk.note.POJO.VO;

import lombok.Data;

import java.util.Date;

@Data
public class NoteVO {
   private String id;

   private String title;

   private String content;

   private Integer number;

   private String username;

   private Boolean encrypt;

   private Date createTime;

   private Date updateTime;

   private Date deleteTime;

   private Integer deleted;

   private Integer version;
}
