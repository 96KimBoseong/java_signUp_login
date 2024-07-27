package com.signup_login.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j(topic = "loggingFilter")
//@Component
@Order(1)//필터의 순서 지정
public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String url = httpServletRequest.getRequestURI();
        log.info(url);
        // 전처리
        filterChain.doFilter(servletRequest, servletResponse);// 다음필터로 이동
        //후처리
        log.info("비즈니스 로직 완료");
    }
}
