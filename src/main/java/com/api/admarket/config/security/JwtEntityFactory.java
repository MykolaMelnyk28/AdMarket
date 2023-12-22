package com.api.admarket.config.security;

import com.api.admarket.api.v1.entity.user.AccountStatus;
import com.api.admarket.api.v1.entity.user.Role;
import com.api.admarket.api.v1.entity.user.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public class JwtEntityFactory {

    public static JwtEntity create(UserEntity user) {
        if (user == null) {
            return null;
        }
        return JwtEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPasswordHash())
                .email(user.getEmail())
                .authorities(mapAuthorities(user.getRoles()))
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(user.getAccountStatus() != AccountStatus.BLOCKED)
                .build();
    }

    private static Set<GrantedAuthority> mapAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(x -> new SimpleGrantedAuthority(x.name()))
                .collect(Collectors.toSet());
    }

}
