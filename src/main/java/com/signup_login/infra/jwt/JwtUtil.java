package com.signup_login.infra.jwt;

import com.signup_login.model.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init(){
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }



    public String createToken(String username, UserRoleEnum role){
        Date date = new Date();

        return BEARER_PREFIX+
                Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public void addJwtToCookie(String token, HttpServletResponse response){
        token = URLEncoder.encode(token, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        Cookie cookie = new Cookie(AUTHORIZATION_HEADER,token); // Name-Value
        cookie.setPath("/");

        response.addCookie(cookie); // Response 객체에 Cookie 추가
    }

    public String substringToken(String tokenValue){
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)){
            return tokenValue.substring(7);
        }
        logger.error("Not found token value");
        throw new NullPointerException("Not found token value");
    }


    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (SecurityException | MalformedJwtException | SignatureException e){
            logger.error("Invalid token, 유효하지 않은 JWT 서명입니다");
        }catch (ExpiredJwtException e){
            logger.error("Unsupported JWT token, 만료된 JWT 토큰입니다");
        }catch (IllegalArgumentException e){
            logger.error("JWT claims is empty, 잘못된 JWT 토큰입니다");
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getTokenFromRequest(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();// 여러개 있는 쿠키들을 배열로
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) { // jwt 형식의 쿠키를 순회하면서 찾음
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
