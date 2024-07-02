package com.aboutdk.note.controller;

import com.aboutdk.note.POJO.DO.UserDO;
import com.aboutdk.note.POJO.DO.WxResponseDO;
import com.aboutdk.note.POJO.FORM.NoteForm;
import com.aboutdk.note.POJO.VO.ResponseVO;
import com.aboutdk.note.POJO.VO.UserVO;
import com.aboutdk.note.client.OrderClient;
import com.aboutdk.note.client.SnowflakeIdClient;
import com.aboutdk.note.client.UserClient;
import com.aboutdk.note.enums.ResponseEnum;
import com.aboutdk.note.POJO.FORM.ChangePasswordForm;
import com.aboutdk.note.POJO.FORM.LoginForm;
import com.aboutdk.note.mapper.mapStructMapper.NoteDOMapper;
import com.aboutdk.note.mapper.mybatisMapper.UserMapper;
import com.aboutdk.note.security.TokenService;
import com.aboutdk.note.service.IUserService;
import com.aboutdk.note.service.impl.NoteServiceImpl;
import com.aboutdk.note.utils.Utils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhouyang
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private NoteServiceImpl noteService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserClient userClient;

    @Autowired
    private SnowflakeIdClient snowflakeIdClient;

    @GetMapping("/loginFromWx")
    private ResponseVO loginFromWeixinMiniprogram(@RequestParam("code") String jsCode) throws Exception {
        log.info("[[loginFromWeixinMiniprogram]]");
        log.info("[[weixin jsCode]] ===> {}", jsCode);

        String APPID = "wx7f36f51f11c3b477";
        String APPSECRET = "05bc8b625c75a904397907c94839e2b1";

        WebClient client = WebClient.create();
        String uri = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=" + APPID +
                "&secret=" + APPSECRET +
                "&js_code=" + jsCode +
                "&grant_type=authorization_code";

        Mono<String> mono = client.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class);


        Gson gson = new Gson();
        WxResponseDO wxResponseDO = gson.fromJson(mono.block(), WxResponseDO.class);
        String openid = wxResponseDO.getOpenid();

        // 先查找数据库有无此用户
        UserDO find = userService.findUserDOByOpenid(openid);
        if (find == null) {
            // 此用户第一次登录，要先注册
            log.info("[[openid: {}]] ==> 未注册，先注册再登录]]", openid);

            HashMap<String, String> map = new HashMap<>();
            map.put("openid", openid);
            map.put("username", openid);
            map.put("password", "fake_password");

            String responseVOStr = userClient.registerWithOpenid(openid);

            ResponseVO responseVO = gson.fromJson(responseVOStr, ResponseVO.class);
            Integer status = responseVO.getStatus();
            if (status != 0) {
                // 注册失败
                throw new Exception("使用 openid 注册失败");
            }
        }

        String MINIPROGRAM_PASSWORD = "miniprogram_password";
        LoginForm loginForm = new LoginForm();
        loginForm.setUsername(openid);
        loginForm.setPassword(Utils.MD5(MINIPROGRAM_PASSWORD));

        return this.loginWithLoginForm(loginForm);
    }

    @PostMapping("/login")
    private ResponseVO<String> loginFromWeb(@Valid @RequestBody LoginForm form, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            log.error("error field is : {} ,message is : {}", fieldErrors.get(0).getField(), fieldErrors.get(0).getDefaultMessage());
            return ResponseVO.error(ResponseEnum.ERROR, fieldErrors.get(0).getField() + " " + fieldErrors.get(0).getDefaultMessage());
        }

        return this.loginWithLoginForm(form);
    }

    private ResponseVO<String> loginWithLoginForm(LoginForm form) throws Exception {
        String username = form.getUsername();
        String md5password = form.getPassword();
        if (username.contains(" ")) {
            return ResponseVO.error(ResponseEnum.ERROR, "username must not contain space");
        }
        if (md5password.contains(" ")) {
            return ResponseVO.error(ResponseEnum.ERROR, "password must not contain space");
        }

        String token = userService.login(username, md5password);
        return ResponseVO.success(token);
    }

    @GetMapping("/{username}/profile")
    private ResponseVO<UserVO> getUserProfile(@PathVariable String username) {
        UserVO userVO = new UserVO();
        UserDO userDO = userService.findUserDO(username);
        BeanUtils.copyProperties(userDO, userVO);

        if ("".equals(userDO.getNotePassword())) {
            userVO.setHasNotePassword(false);
        } else {
            userVO.setHasNotePassword(true);
        }

        return ResponseVO.success(userVO);
    }

    @PutMapping("/profile")
    private ResponseVO<UserVO> updateProfile(@RequestBody UserDO userDO) {
        UserDO newUserDO = userService.updateProfile(userDO);

        String username = newUserDO.getUsername();
        ResponseVO<UserVO> userProfile = this.getUserProfile(username);
        return userProfile;
    }

    @PostMapping("/setNotePassword/{notePassword}")
    public ResponseVO setNotePassword(@PathVariable String notePassword, @RequestHeader HttpHeaders headers) {
        String curUser = tokenService.getUsername(headers.getFirst("token"));

        UserDO userDO = userService.findUserDO(curUser);
        if ("null".equals(notePassword)) {
            // cancel not password
            userDO.setNotePassword("");

            // set all note encrypt to false
            List<NoteForm> noteFormList = noteService.findAllByUsername(curUser)
                    .stream()
                    .map((noteDO) -> {
                        NoteForm noteForm = NoteDOMapper.INSTANCE.noteDOToNoteForm(noteDO);
                        noteForm.setEncrypt(false);
                        return noteForm;
                    })

                    .collect(Collectors.toList());

            for (NoteForm noteForm : noteFormList) {
                noteService.updateNote(curUser, noteForm);
            }
        } else {
            userDO.setNotePassword(notePassword);
        }
        userMapper.updateById(userDO);

        return ResponseVO.success();
    }

    @PostMapping("/validateNotePassword/{notePassword}")
    private ResponseVO validateNotePassword(@PathVariable String notePassword, @RequestHeader HttpHeaders headers) {
        String curUser = tokenService.getUsername(headers.getFirst("token"));

        UserDO userDO = userService.findUserDO(curUser);
        if (!userDO.getNotePassword().equals(notePassword)) {
            throw new RuntimeException("note password is wrong");
        }
        return ResponseVO.success("note password is validated");
    }

    @PutMapping("/changePassword")
    private ResponseVO<UserDO> changePassword(@Valid @RequestBody ChangePasswordForm form) {
        UserDO userDO = userService.changePassword(form);
        return ResponseVO.success(userDO);
    }

    @DeleteMapping
    private ResponseVO delete(@RequestBody LoginForm form) {
        userService.delete(form);

        return ResponseVO.success("删除成功");
    }
}
