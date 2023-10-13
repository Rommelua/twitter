package com.example.twitter.mapper.impl

import com.example.twitter.dto.post.CommentDto
import com.example.twitter.dto.user.UserResponseDtoWithoutSubscriptions
import com.example.twitter.mapper.CommentMapper
import com.example.twitter.mapper.UserMapper
import com.example.twitter.model.Comment
import org.springframework.stereotype.Component

@Component
class CommentMapperImpl implements CommentMapper {
    private final UserMapper userMapper

    CommentMapperImpl(UserMapper userMapper) {
        this.userMapper = userMapper
    }

    @Override
    CommentDto toDto(Comment comment) {
        UserResponseDtoWithoutSubscriptions user = userMapper.toDtoWithoutSubscriptions(comment.user)
        return new CommentDto(id: comment.id, content: comment.content,
                date: comment.date, user: user)
    }
}
