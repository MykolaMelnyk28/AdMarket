package com.api.admarket.config;

import com.api.admarket.api.v1.repository.UserRepository;
import com.api.admarket.config.props.MinioProperties;
import com.api.admarket.config.security.JwtAuthenticationFilter;
import com.api.admarket.config.security.SimpleUserDetailsService;
import io.minio.MinioClient;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@AllArgsConstructor
public class ApplicationConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new SimpleUserDetailsService(userRepository);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AdMarket API")
                        .description("")
                        .version("1.0")
                )
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth")
                )
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthFilter
    ) throws Exception {
        //http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(request -> {
            request.anyRequest().permitAll();
        });
        http.exceptionHandling(handling -> {
            handling.authenticationEntryPoint((request, response, exception) -> {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Unauthorized");
            });

            handling.accessDeniedHandler(((request, response, exception) -> {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write("Unauthorized");
            }));
        });
        http.logout(x -> {
            x.logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"));
            x.logoutSuccessHandler((request, response, authentication) ->
                    response.setStatus(HttpServletResponse.SC_OK));
            x.deleteCookies("JSESSIONID");
        });
        http.sessionManagement(sessions -> {
            sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });
        http.anonymous(AbstractHttpConfigurer::disable);
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public MinioClient minioClient(MinioProperties minioProps) {
        return MinioClient.builder()
                .endpoint(minioProps.getUrl())
                .credentials(minioProps.getAccessKey(), minioProps.getSecretKey())
                .build();
    }
}
