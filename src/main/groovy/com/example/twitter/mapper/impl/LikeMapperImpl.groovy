package com.example.twitter.mapper.impl

import com.example.twitter.dto.post.LikeDto
import com.example.twitter.dto.user.UserResponseDtoWithoutSubscriptions
import com.example.twitter.mapper.LikeMapper
import com.example.twitter.mapper.UserMapper
import com.example.twitter.model.Like
import org.springframework.stereotype.Component

@Component
class LikeMapperImpl implements LikeMapper {
    private final UserMapper userMapper

    LikeMapperImpl(UserMapper userMapper) {
        this.userMapper = userMapper
    }

    @Override
    LikeDto toDto(Like like) {
        UserResponseDtoWithoutSubscriptions user = userMapper.toDtoWithoutSubscriptions(like.user)
        return new LikeDto(user: user)
    }
}
