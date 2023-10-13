package com.example.twitter.controller

import com.example.twitter.dto.user.UserRegistrationRequestDto
import com.example.twitter.dto.user.UserResponseDto
import com.example.twitter.model.User
import com.example.twitter.service.UserService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@Tag(name = "Users", description = "Endpoints for user operations")
@RestController()
@RequestMapping("/users")
class UserController {
    private final UserService userService

    UserController(UserService userService) {
        this.userService = userService
    }

    @PutMapping()
    UserResponseDto update(Authentication authentication, @RequestBody UserRegistrationRequestDto dto) {
        User user = (User) authentication.getPrincipal()
        return userService.update(user.id, dto)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping()
    void delete(Authentication authentication) {
        User user = (User) authentication.getPrincipal()
        userService.deleteById(user.id)
    }

    @PutMapping("/{toSubscribeId}/subscribe")
    UserResponseDto addSubscription(Authentication authentication, @PathVariable String toSubscribeId) {
        User user = (User) authentication.getPrincipal()
        return userService.addSubscription(user.id, toSubscribeId)
    }

    @PutMapping("/{toUnsubscribeId}/unsubscribe")
    UserResponseDto deleteSubscription(Authentication authentication, @PathVariable String toUnsubscribeId) {
        User user = (User) authentication.getPrincipal()
        return userService.removeSubscription(user.id, toUnsubscribeId)
    }
}
