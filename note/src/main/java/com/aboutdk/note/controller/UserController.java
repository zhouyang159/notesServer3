package com.aboutdk.note.controller;

import com.aboutdk.note.POJO.DO.UserDO;
import com.aboutdk.note.POJO.FORM.NoteForm;
import com.aboutdk.note.POJO.VO.ResponseVO;
import com.aboutdk.note.POJO.VO.UserVO;
import com.aboutdk.note.enums.ResponseEnum;
import com.aboutdk.note.POJO.FORM.ChangePasswordForm;
import com.aboutdk.note.POJO.FORM.LoginForm;
import com.aboutdk.note.POJO.FORM.RegisterUserForm;
import com.aboutdk.note.mapper.mapStructMapper.NoteDOMapper;
import com.aboutdk.note.mapper.mybatisMapper.UserMapper;
import com.aboutdk.note.security.TokenService;
import com.aboutdk.note.service.IUserService;
import com.aboutdk.note.service.impl.NoteServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

   @PostMapping("/register")
   private ResponseVO<UserDO> register(@Valid @RequestBody RegisterUserForm form, BindingResult bindingResult) {
      if (bindingResult.hasErrors()) {
         List<FieldError> fieldErrors = bindingResult.getFieldErrors();
         log.error("error field is : {} ,message is : {}", fieldErrors.get(0).getField(), fieldErrors.get(0).getDefaultMessage());
         return ResponseVO.error(ResponseEnum.ERROR, fieldErrors.get(0).getField() + " " + fieldErrors.get(0).getDefaultMessage());
      }

      if (form.getUsername().trim().contains(" ")) {
         return ResponseVO.error(ResponseEnum.ERROR, "username must not contain space");
      }
      if (form.getPassword().trim().contains(" ")) {
         return ResponseVO.error(ResponseEnum.ERROR, "password must not contain space");
      }

      UserDO userDO = userService.register(form);
      return ResponseVO.success(userDO);
   }

   @PostMapping("/login")
   private ResponseVO<String> login(@Valid @RequestBody LoginForm form, BindingResult bindingResult) throws Exception {
      if (bindingResult.hasErrors()) {
         List<FieldError> fieldErrors = bindingResult.getFieldErrors();
         log.error("error field is : {} ,message is : {}", fieldErrors.get(0).getField(), fieldErrors.get(0).getDefaultMessage());
         return ResponseVO.error(ResponseEnum.ERROR, fieldErrors.get(0).getField() + " " + fieldErrors.get(0).getDefaultMessage());
      }

      String username = form.getUsername();
      String password = form.getPassword();
      if (username.contains(" ")) {
         return ResponseVO.error(ResponseEnum.ERROR, "username must not contain space");
      }
      if (password.contains(" ")) {
         return ResponseVO.error(ResponseEnum.ERROR, "password must not contain space");
      }

      String token = userService.login(username, password);
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
