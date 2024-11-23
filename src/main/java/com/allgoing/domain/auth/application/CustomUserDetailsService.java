package com.allgoing.domain.auth.application;


import com.allgoing.domain.user.domain.User;
import com.allgoing.domain.user.domain.repository.UserRepository;
import com.allgoing.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Spring Security에서 username을 이름으로 간주
        log.info("Loading user by name: {}", username);
        return loadUserByName(username);
    }

    public UserDetails loadUserByName(String name) throws UsernameNotFoundException {
        User user = userRepository.findByName(name) // 이름으로 사용자 검색
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name: " + name));
        return UserPrincipal.create(user);
    }
}