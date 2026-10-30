package com.ibrahimahmads.github.vbooth.security.service;

import com.ibrahimahmads.github.vbooth.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String token = jwtUtil.generateToken(authentication);
        ResponseCookie cookie = jwtUtil.generateJwtCookie(token);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        String targetUrl = "http://localhost:5173/dashboard";
        response.sendRedirect(targetUrl);
    }
}
