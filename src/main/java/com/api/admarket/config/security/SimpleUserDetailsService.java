package com.api.admarket.config.security;

import com.api.admarket.api.v1.entity.user.UserEntity;
import com.api.admarket.api.v1.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
public class SimpleUserDetailsService implements UserDetailsService {

    private final UserRepository userService;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<UserEntity> userOpt = userService.findByUsername(username);
        UserEntity user = userOpt.orElseThrow(() ->
                new UsernameNotFoundException("Username not found.")
        );
        return JwtEntityFactory.create(user);
    }

}
