//package com.aboutdk.user.controller;
//
//import com.aboutdk.user.POJO.DO.UserDO;
//import com.aboutdk.user.POJO.VO.ResponseVO;
//import com.aboutdk.user.mybatisMapper.UserMapper;
//import com.aboutdk.user.service.IUserService;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class UserControllerTest {
//
//   @Autowired
//   private IUserService userService;
//
//   @Autowired
//   private UserMapper userMapper;
//
//   @Autowired
//   private TestRestTemplate restTemplate;
//
//   @Test
//   public void testRegisterValid() {
//
//      // Create valid registration form object
//      // request body parameters
//      Map<String, String> map = new HashMap<>();
//      String username = "testuser";
//      map.put("username", "testuser");
//      map.put("password", "testpassword");
//
//      // Send POST request to /user endpoint with valid form data
//      ResponseEntity<ResponseVO> responseEntity =
//              restTemplate.postForEntity("/user", map, ResponseVO.class);
//
//      // Verify that the HTTP status code is 200 OK
//      assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//
//      // Verify that the response body contains a non-null UserDO object with the correct username
//      ResponseVO<UserDO> responseBody = responseEntity.getBody();
//      assertNotNull(responseBody.getData());
//
//
//      // Clean up database by deleting test user
//      QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
//      queryWrapper.eq("username", "testuser");
//      UserDO userDO = userMapper.selectOne(queryWrapper);
//      userMapper.deleteById(userDO.getId());
//
//   }
//}