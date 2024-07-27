package com.signup_login.controller;

import com.signup_login.entity.UserRoleEnum;
import com.signup_login.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/create-jwt")
    public String createJwt(HttpServletResponse response){

        String token = jwtUtil.createToken("KBS", UserRoleEnum.USER);

        jwtUtil.addJwtToCookie(token,response);
        return "createJwt success : " + token;
    }

    @GetMapping("/get-jwt")
    public String getJwt(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {

        String token = jwtUtil.substringToken(tokenValue);

        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("token error");
        }

        Claims info = jwtUtil.getUserInfoFromToken(token);


        String username = info.getSubject();
        System.out.println("username: " + username);


        String authority = (String) info.get(JwtUtil.AUTHORIZATION_KEY);
        System.out.println("authority: " + authority);


        return "getJwt success : " + username + " " + authority;
    }
    }

