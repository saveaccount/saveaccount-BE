package com.example.savemoney.jwt;

import com.example.savemoney.dto.CustomUserDetails;
import com.example.savemoney.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = request.getHeader("access");

        // 토큰이 없을 경우
        if(accessToken==null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰이 만료되었을 경우
        try{
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            log.error("토큰이 만료되었습니다.");
            response.getWriter().write("토큰이 만료되었습니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // 만료되었을 경우 필터체인을 끊어버림
            return;
        }

        // 토큰이 access인지 확인
        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {
            log.error("access 토큰이 아닙니다.");
            response.getWriter().write("access 토큰이 아닙니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }


        String username = jwtUtil.getUsername(accessToken);
//        String role = jwtUtil.getRole(accessToken);

        User user = User.builder().username(username).password("temp").build();


        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
