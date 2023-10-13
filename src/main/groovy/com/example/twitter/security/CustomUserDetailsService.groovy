package com.example.twitter.security

import com.example.twitter.model.User
import com.example.twitter.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository

    CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository
    }

    @Override
    UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
        if (user == null) {
            throw new UsernameNotFoundException("User is not found by email " + email)
        }
        return user
    }
}
