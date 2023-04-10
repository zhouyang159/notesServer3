package com.aboutdk.POJO.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "note_users")
public class UserDO {

    private long id;

    private String username;

    private String nickname;

    private String backgroundColor;

    private Integer age;

    private String notePassword;

    private String password;

    private Date createTime;

    private Date updateTime;

    private Integer autoLogout;
}
