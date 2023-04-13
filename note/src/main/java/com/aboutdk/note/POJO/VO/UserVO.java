package com.aboutdk.note.POJO.VO;

import lombok.Data;

@Data
public class UserVO {

   private long id;

   private String username;

   private String nickname;

   private String backgroundColor;

   private Integer age;

   private Boolean hasNotePassword;

   private Integer autoLogout;
}
