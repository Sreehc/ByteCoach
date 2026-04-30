package com.bytecoach.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytecoach.user.entity.User;
import com.bytecoach.user.vo.UserInfoVO;

public interface UserService extends IService<User> {
    User getByUsername(String username);

    User createUser(String username, String password, String nickname);

    UserInfoVO getCurrentUserInfo();
}

