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
        log.info("Attempting to load user by username: {}", username);
        return loadUserByName(username);
    }

    public UserDetails loadUserByName(String name) throws UsernameNotFoundException {
        log.info("Searching for user by name: {}", name);
        return userRepository.findByName(name)
                .map(user -> {
                    log.info("User found: {}", user);
                    return UserPrincipal.create(user);
                })
                .orElseThrow(() -> {
                    log.warn("User not found with name: {}", name);
                    return new UsernameNotFoundException("User not found with name: " + name);
                });
    }
}