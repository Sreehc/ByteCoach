package com.bytecoach.user.controller;

import com.bytecoach.common.api.Result;
import com.bytecoach.user.service.UserService;
import com.bytecoach.user.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public Result<UserInfoVO> me() {
        return Result.success(userService.getCurrentUserInfo());
    }
}

