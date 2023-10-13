package com.example.twitter.dto.user

class UserResponseDto {
    String id
    String email
    String firstName
    String lastName
    List<UserResponseDtoWithoutSubscriptions> subscriptions
}
