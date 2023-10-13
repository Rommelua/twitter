package com.example.twitter.dto.post

import com.example.twitter.dto.user.UserResponseDtoWithoutSubscriptions

import java.time.LocalDateTime

class CommentDto {
    String id
    String content
    LocalDateTime date
    UserResponseDtoWithoutSubscriptions user
}
