package com.example.twitter.mapper.impl

import com.example.twitter.dto.user.UserRegistrationRequestDto
import com.example.twitter.dto.user.UserResponseDto
import com.example.twitter.dto.user.UserResponseDtoWithoutSubscriptions
import com.example.twitter.mapper.UserMapper
import com.example.twitter.model.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

import java.util.stream.Collectors

@Component
class UserMapperImpl implements UserMapper {
    private final PasswordEncoder passwordEncoder

    UserMapperImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder
    }

    @Override
    UserResponseDto toDto(User user) {
        def subscriptions = user.subscriptions.stream()
                .map(u -> toDtoWithoutSubscriptions(u))
                .collect(Collectors.toList())
        return new UserResponseDto(id: user.id, email: user.email, firstName: user.firstName,
                lastName: user.lastName, subscriptions: subscriptions)
    }

    @Override
    UserResponseDtoWithoutSubscriptions toDtoWithoutSubscriptions(User user) {
        return new UserResponseDtoWithoutSubscriptions(id: user.id, email: user.email,
                firstName: user.firstName, lastName: user.lastName)
    }

    @Override
    User toModel(UserRegistrationRequestDto dto) {
        String encodedPassword = passwordEncoder.encode(dto.password)
        return new User(email: dto.email, password: encodedPassword,
                firstName: dto.firstName, lastName: dto.lastName)
    }

    @Override
    User updateProperties(User user, UserRegistrationRequestDto dto) {
        user.email = dto.email
        user.password = passwordEncoder.encode(dto.password)
        user.firstName = dto.firstName
        user.lastName = dto.lastName
        return user
    }
}
