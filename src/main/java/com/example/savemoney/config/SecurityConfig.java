package com.example.savemoney.config;

import com.example.savemoney.jwt.JWTFilter;
import com.example.savemoney.jwt.JWTUtil;
import com.example.savemoney.jwt.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    private final JWTUtil jwtUtil;

//    private final RefreshRepository refreshRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors((cors) -> cors
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration corsConfiguration = new CorsConfiguration();
                                corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                                corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                                corsConfiguration.setAllowCredentials(true);
                                corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                                corsConfiguration.setMaxAge(3600L);

                                corsConfiguration.setExposedHeaders(Collections.singletonList("Authorization"));
                                corsConfiguration.addExposedHeader("access");
                                return corsConfiguration;
                            }
                        }));

        http
                .csrf((auth) -> auth
                        .disable());

        http
                .formLogin((auth) -> auth
                        .disable());

        http
                .httpBasic((auth) -> auth
                        .disable());

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/auth/signup", "/login","/reissue", "/user/check-id").permitAll()
//                        .requestMatchers("/admin").hasAnyRole("ADMIN")
                        .anyRequest().authenticated());

        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil), UsernamePasswordAuthenticationFilter.class);





        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));




        return http.build();
    }
}
