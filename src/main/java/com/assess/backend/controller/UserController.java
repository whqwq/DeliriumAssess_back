package com.assess.backend.controller;

import com.assess.backend.service.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assess.backend.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/login")
    public ResponseEntity<APIResponse> login(@RequestBody Map<String, Object> data) {
        return userService.login(data);
    }

    @RequestMapping("/getAllUsers")
    public ResponseEntity<APIResponse> getAllUsers(@RequestHeader("phone") String phone) {
        return userService.getAllUsers(phone);
    }

    @RequestMapping("/createUser")
    public ResponseEntity<APIResponse> createUser(@RequestBody Map<String, Object> data) {
        return userService.createUser(data);
    }

    @RequestMapping("/changePassword")
    public ResponseEntity<APIResponse> changePassword(@RequestBody Map<String, Object> data) {
        return userService.changePassword(data);
    }

    @RequestMapping("/resetPassword")
    public ResponseEntity<APIResponse> resetPassword(@RequestBody Map<String, Object> data) {
        return userService.resetPassword(data);
    }

    @RequestMapping("/deleteUser")
    public ResponseEntity<APIResponse> deleteUser(@RequestBody Map<String, Object> data) {
        return userService.deleteUser(data);
    }

    @RequestMapping("/changeUserInfo")
    public ResponseEntity<APIResponse> changeUserInfo(@RequestBody Map<String, Object> data) {
        return userService.changeUserInfo(data);
    }
}
