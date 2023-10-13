package com.example.twitter.service

import com.example.twitter.dto.user.UserRegistrationRequestDto
import com.example.twitter.dto.user.UserResponseDto
import com.example.twitter.exception.EntityNotFoundException
import com.example.twitter.exception.RegistrationException
import com.example.twitter.mapper.UserMapper
import com.example.twitter.model.User
import com.example.twitter.repository.UserRepository
import com.example.twitter.service.impl.UserServiceImpl
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

class UserServiceImplSpec extends Specification {
    UserRepository userRepository = Mock()
    PasswordEncoder passwordEncoder = Mock()
    UserMapper userMapper = Mock()
    UserServiceImpl service = new UserServiceImpl(userRepository, passwordEncoder, userMapper)

    def "register | OK"() {
        given:
        String email = "test@email.com"
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto(email: email)
        User user = new User(email: email)
        UserResponseDto expected = new UserResponseDto(email: email)

        when:
        UserResponseDto actual = service.register(requestDto)

        then:
        1 * userRepository.existsByEmail(email) >> false
        1 * userMapper.toModel(requestDto) >> user
        1 * userRepository.save(user) >> user
        1 * userMapper.toDto(user) >> expected
        0 * _
        actual == expected
    }

    def "register | email already exist | NOT OK"() {
        given:
        String email = "test@email.com"
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto(email: email)

        when:
        service.register(requestDto)

        then:
        1 * userRepository.existsByEmail(email) >> true
        0 * _
        thrown(RegistrationException)
    }

    def "update | OK"() {
        given:
        String id = "123"
        String email = "test@email.com"
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto(email: email)
        User user = new User(email: email)
        UserResponseDto expected = new UserResponseDto(email: email)

        when:
        UserResponseDto actual = service.update(id, requestDto)

        then:
        1 * userRepository.findById(id) >> Optional.of(user)
        1 * userMapper.updateProperties(user, requestDto) >> user
        1 * userRepository.save(user) >> user
        1 * userMapper.toDto(user) >> expected
        0 * _
        actual == expected
    }

    def "update | invalid user id | NOT OK"() {
        given:
        String id = "123"
        String email = "test@email.com"
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto(email: email)

        when:
        service.update(id, requestDto)

        then:
        1 * userRepository.findById(id) >> Optional.empty()
        0 * _
        thrown(EntityNotFoundException)
    }

    def "deleteById | OK"() {
        given:
        String id = "123"

        when:
        service.deleteById(id)

        then:
        1 * userRepository.deleteById(id)
        0 * _
    }

    def "addSubscription | OK"() {
        given:
        String userId = "123"
        String toSubscribeId = "456"
        String email = "test@email.com"
        User user = new User(email: email)
        User userToSubscribe = new User(email: "sub@email.com")
        UserResponseDto expected = new UserResponseDto(email: email)

        when:
        UserResponseDto actual = service.addSubscription(userId, toSubscribeId)

        then:
        1 * userRepository.findById(userId) >> Optional.of(user)
        1 * userRepository.findById(toSubscribeId) >> Optional.of(userToSubscribe)
        1 * userRepository.save(user) >> user
        1 * userMapper.toDto(user) >> expected
        0 * _
        actual == expected
        user.subscriptions.contains(userToSubscribe)
    }

    def "addSubscription | invalid user id | NOT OK"() {
        given:
        String userId = "123"
        String toSubscribeId = "456"

        when:
        service.addSubscription(userId, toSubscribeId)

        then:
        1 * userRepository.findById(userId) >> Optional.empty()
        0 * _
        thrown(EntityNotFoundException)
    }

    def "addSubscription | invalid toSubscribeId | NOT OK"() {
        given:
        String userId = "123"
        String toSubscribeId = "456"
        User user = new User()

        when:
        service.addSubscription(userId, toSubscribeId)

        then:
        1 * userRepository.findById(userId) >> Optional.of(user)
        1 * userRepository.findById(toSubscribeId) >> Optional.empty()
        0 * _
        thrown(EntityNotFoundException)
    }

    def "removeSubscription | OK"() {
        given:
        String userId = "123"
        String toUnsubscribeId = "456"
        String email = "test@email.com"
        User user = new User(email: email)
        User userToUnsubscribe = new User(email: "sub@email.com")
        user.subscriptions.add(userToUnsubscribe)
        UserResponseDto expected = new UserResponseDto(email: email)

        when:
        UserResponseDto actual = service.removeSubscription(userId, toUnsubscribeId)

        then:
        1 * userRepository.findById(userId) >> Optional.of(user)
        1 * userRepository.findById(toUnsubscribeId) >> Optional.of(userToUnsubscribe)
        1 * userRepository.save(user) >> user
        1 * userMapper.toDto(user) >> expected
        0 * _
        actual == expected
        !user.subscriptions.contains(userToUnsubscribe)
    }

    def "removeSubscription | invalid user id | NOT OK"() {
        given:
        String userId = "123"
        String toSubscribeId = "456"

        when:
        service.removeSubscription(userId, toSubscribeId)

        then:
        1 * userRepository.findById(userId) >> Optional.empty()
        0 * _
        thrown(EntityNotFoundException)
    }

    def "removeSubscription | invalid toSubscribeId | NOT OK"() {
        given:
        String userId = "123"
        String toSubscribeId = "456"
        User user = new User()

        when:
        service.removeSubscription(userId, toSubscribeId)

        then:
        1 * userRepository.findById(userId) >> Optional.of(user)
        1 * userRepository.findById(toSubscribeId) >> Optional.empty()
        0 * _
        thrown(EntityNotFoundException)
    }
}
