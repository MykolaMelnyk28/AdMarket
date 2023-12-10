package com.api.admarket.config.security;

import com.api.admarket.config.props.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProps;
    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProps.getSecret().getBytes());
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateAccessToken(userDetails, new HashMap<>());
    }

    public String generateAccessToken(
            UserDetails userDetails,
            Map<String, Object> extraClaims
    ) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProps.getAccess());

        Set<String> roles = convertToStrRoles(userDetails.getAuthorities());

        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProps.getRefresh());

        Set<String> roles = convertToStrRoles(userDetails.getAuthorities());

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    private Set<String> convertToStrRoles(Collection<? extends GrantedAuthority> authorities) {
        Set<? extends GrantedAuthority> authoritiesSet = (Set<? extends GrantedAuthority>)authorities;
        return authoritiesSet.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);

        List<String> rolesList = (List<String>)extractClaim(token, claims -> claims.get("roles"));
        Set<String> roles = new HashSet<>(rolesList);

        Set<String> userDetailsRoles = convertToStrRoles(userDetails.getAuthorities());

        return userDetails.getUsername().equals(username) &&
                userDetailsRoles.equals(roles) &&
                !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        final Date now = new Date();
        return extractExpiration(token).before(now);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
