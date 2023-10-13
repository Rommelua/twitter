package com.example.twitter.security

import com.example.twitter.dto.user.UserLoginRequestDto
import com.example.twitter.dto.user.UserLoginResponseDto
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class AuthenticationService {
    private final JwtUtil jwtUtil
    private final AuthenticationManager authenticationManager

    AuthenticationService(JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil
        this.authenticationManager = authenticationManager
    }

    public UserLoginResponseDto login(UserLoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        )
        String token = jwtUtil.generateToken(request.getEmail())
        return new UserLoginResponseDto(token)
    }
}
