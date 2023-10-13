package com.example.twitter.controller

import com.example.twitter.dto.user.UserLoginRequestDto
import com.example.twitter.dto.user.UserLoginResponseDto
import com.example.twitter.dto.user.UserRegistrationRequestDto
import com.example.twitter.dto.user.UserResponseDto
import com.example.twitter.security.AuthenticationService
import com.example.twitter.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

import javax.validation.Valid

@RestController()
@RequestMapping("/auth")
class AuthenticationController {
    private final UserService userService
    private final AuthenticationService authenticationService

    AuthenticationController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService
        this.authenticationService = authenticationService
    }

    @PostMapping("/login")
    UserLoginResponseDto login(@RequestBody UserLoginRequestDto request) {
        return authenticationService.login(request)
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/register")
    UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request) {
        return userService.register(request)
    }
}
