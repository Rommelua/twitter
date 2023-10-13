package com.example.twitter.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil
    private final UserDetailsService userDetailsService

    JwtTokenFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil
        this.userDetailsService = userDetailsService
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.resolveToken(request)
        if (jwtUtil.isTokenValid(token)) {
            String userName = jwtUtil.getUserName(token)
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName)
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            )
            SecurityContextHolder.getContext().setAuthentication(authentication)
        }
        filterChain.doFilter(request, response)
    }
}
