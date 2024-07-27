package com.signup_login.controller;

import com.signup_login.model.User;
import com.signup_login.model.UserRoleEnum;
import com.signup_login.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class ProductController {

    @GetMapping("/products")
    public String getProducts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        // Authentication 의 Principal 에 저장된 UserDetailsImpl 을 가져옵니다.
        User user =  userDetails.getUser();
        System.out.println("user.getUsername() = " + user.getUsername());
        System.out.println(user.getEmail());
        System.out.println(user.getRole());

        return "redirect:/";
    }

    @Secured(UserRoleEnum.Authority.ADMIN)
    @GetMapping("/products/secured")
    public String getProductByAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails){
        System.out.println("userDetails.getUsername() = " + userDetails.getUsername());
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            System.out.println("authority.getAuthority() = " + authority.getAuthority());
        }

        return "redirect:/";
    }
}
