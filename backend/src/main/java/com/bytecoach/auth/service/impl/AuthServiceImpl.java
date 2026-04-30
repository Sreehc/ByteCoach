package com.bytecoach.auth.service.impl;

import com.bytecoach.auth.dto.LoginRequest;
import com.bytecoach.auth.dto.LoginResponse;
import com.bytecoach.auth.dto.RegisterRequest;
import com.bytecoach.auth.service.AuthService;
import com.bytecoach.security.model.LoginUser;
import com.bytecoach.security.util.JwtTokenUtil;
import com.bytecoach.user.entity.User;
import com.bytecoach.user.service.UserService;
import com.bytecoach.user.vo.UserInfoVO;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public LoginResponse register(RegisterRequest request) {
        User user = userService.createUser(request.getUsername(), request.getPassword(), request.getNickname());
        user.setLastLoginTime(LocalDateTime.now());
        userService.updateById(user);
        return buildLoginResponse(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User user = loginUser.getUser();
        user.setLastLoginTime(LocalDateTime.now());
        userService.updateById(user);
        return buildLoginResponse(user);
    }

    private LoginResponse buildLoginResponse(User user) {
        UserInfoVO userInfo = UserInfoVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .build();
        return LoginResponse.builder()
                .token(jwtTokenUtil.generateToken(user.getId(), user.getUsername()))
                .userInfo(userInfo)
                .build();
    }

    @Override
    public void logout() {
        // Stateless JWT logout remains a no-op for MVP. Redis blacklist can be added later.
    }
}
