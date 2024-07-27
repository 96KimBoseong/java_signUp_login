package com.signup_login.service;

import com.signup_login.dto.LoginRequestDTO;
import com.signup_login.dto.SignupRequestDto;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    public void signup(SignupRequestDto requestDto);
    public void login(LoginRequestDTO requestDto, HttpServletResponse response);

}
