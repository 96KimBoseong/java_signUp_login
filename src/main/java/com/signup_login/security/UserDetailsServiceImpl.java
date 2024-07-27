package com.signup_login.security;

import com.signup_login.model.User;
import com.signup_login.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    // UserDetailsService / UserDetails 를 커스텀하게 되면 시큐리티의 기본 로그인 기능을 사용하지 않겠다
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));
        // 유저이름으로 유저 찾고
        return new UserDetailsImpl(user);
        // 유저객체를 디테일에 넣고 객체 생성으로 토스
    }
}
