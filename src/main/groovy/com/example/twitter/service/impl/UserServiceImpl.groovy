package com.example.twitter.service.impl

import com.example.twitter.dto.user.UserRegistrationRequestDto
import com.example.twitter.dto.user.UserResponseDto
import com.example.twitter.exception.EntityNotFoundException
import com.example.twitter.exception.RegistrationException
import com.example.twitter.mapper.UserMapper

import com.example.twitter.model.User
import com.example.twitter.repository.UserRepository
import com.example.twitter.service.UserService
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl implements UserService {
    private final UserRepository userRepository
    private final UserMapper userMapper

    UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository
        this.userMapper = userMapper
    }

    @Override
    @Transactional
    UserResponseDto register(UserRegistrationRequestDto dto) {
        if (userRepository.existsByEmail(dto.email)) {
            throw new RegistrationException("Email already exist " + dto.email);
        }
        User user = userMapper.toModel(dto)
        user = userRepository.save(user)
        return userMapper.toDto(user)
    }

    @Override
    @Transactional
    UserResponseDto update(String id, UserRegistrationRequestDto dto) {
        User user = getUserById(id)
        user = userMapper.updateProperties(user, dto)
        return userMapper.toDto(userRepository.save(user))

    }

    @Override
    @Transactional
    void deleteById(String id) {
        userRepository.deleteById(id)
    }

    @Override
    @Transactional
    UserResponseDto addSubscription(String userId, String toSubscribeId) {
        User user = getUserById(userId)
        User userToSubscribe = getUserById(toSubscribeId)
        user.subscriptions.add(userToSubscribe)
        return userMapper.toDto(userRepository.save(user))
    }

    @Override
    @Transactional
    UserResponseDto removeSubscription(String userId, String toUnsubscribeId) {
        User user = getUserById(userId)
        User userToSubscribe = getUserById(toUnsubscribeId)
        user.subscriptions.remove(userToSubscribe)
        return userMapper.toDto(userRepository.save(user))
    }

    private User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Can't found user with id: " + userId))
    }
}
