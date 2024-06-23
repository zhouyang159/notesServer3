package com.aboutdk.note.POJO.VO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoteVO {
   private String id;

   private String title;

   private String content;

   private Integer number;

   private String username;

   private Boolean encrypt;

   private LocalDateTime createTime;

   private LocalDateTime updateTime;

   private LocalDateTime deleteTime;

   private Integer deleted;

   private Integer version;
}
