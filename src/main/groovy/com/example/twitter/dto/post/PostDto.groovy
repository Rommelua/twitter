package com.example.twitter.dto.post

import com.example.twitter.dto.user.UserResponseDtoWithoutSubscriptions

import java.time.LocalDateTime

class PostDto {
    String id
    UserResponseDtoWithoutSubscriptions user
    String content
    LocalDateTime date
    List<CommentDto> comments
    List<LikeDto> likes
}
