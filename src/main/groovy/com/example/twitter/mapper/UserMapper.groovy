package com.example.twitter.mapper

import com.example.twitter.dto.user.UserRegistrationRequestDto
import com.example.twitter.dto.user.UserResponseDto
import com.example.twitter.dto.user.UserResponseDtoWithoutSubscriptions
import com.example.twitter.model.User

interface UserMapper {
    UserResponseDto toDto(User user)

    UserResponseDtoWithoutSubscriptions toDtoWithoutSubscriptions(User user)

    User toModel(UserRegistrationRequestDto dto)

    User updateProperties(User user, UserRegistrationRequestDto dto)
}