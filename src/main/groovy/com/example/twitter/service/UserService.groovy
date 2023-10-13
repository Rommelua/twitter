package com.example.twitter.service

import com.example.twitter.dto.user.UserRegistrationRequestDto
import com.example.twitter.dto.user.UserResponseDto

interface UserService {
    UserResponseDto register(UserRegistrationRequestDto dto)

    UserResponseDto update(String id, UserRegistrationRequestDto dto)

    void deleteById(String id)

    UserResponseDto addSubscription(String userId, String toSubscribeId)

    UserResponseDto removeSubscription(String userId, String toUnsubscribeId)
}